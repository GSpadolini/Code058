package com.code058.modelo.dao;

import com.code058.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public int insertar(Pedido p) throws SQLException {
        String sql = "INSERT INTO pedido (cliente_email, articulo_codigo, unidades, fecha_hora) VALUES (?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getCliente().getEmail());
            ps.setString(2, p.getArticulo().getCodigo());
            ps.setInt(3, p.getCantidad()); // en tu POJO es getCantidad()
            ps.setTimestamp(4, Timestamp.valueOf(p.getFechaPedido()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int nuevoId = rs.getInt(1);
                    p.setNumeroPedido(nuevoId);
                    return nuevoId;
                }
            }
        }
        return -1;
    }


    public boolean eliminarSiCancelable(int numeroPedido) throws SQLException {
        // Comprobamos cancelable con TIMESTAMPDIFF usando la hora del pedido y el tiempo de preparación del artículo
        String q = """
        SELECT TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) AS minutos,
               COALESCE(a.tiempo_preparacion_min, 0) AS tiempoPreparacion
        FROM pedido p
        LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
        WHERE p.numero_pedido = ?
    """;
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(q)) {
            ps.setInt(1, numeroPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false; // no existe
                int minutos = rs.getInt("minutos");
                int prep = rs.getInt("tiempoPreparacion");
                if (minutos < prep) {
                    // cancelable -> eliminar
                    try (PreparedStatement del = c.prepareStatement("DELETE FROM pedido WHERE numero_pedido = ?")) {
                        del.setInt(1, numeroPedido);
                        del.executeUpdate();
                        return true;
                    }
                } else {
                    return false; // no cancelable
                }
            }
        }
    }

    public List<Pedido> obtenerTodosBasico() throws SQLException {
        String sql = """
        SELECT
          p.numero_pedido            AS numeroPedido,
          p.cliente_email           AS emailCliente,
          p.articulo_codigo         AS codigoArticulo,
          p.unidades                AS cantidad,
          p.fecha_hora              AS fechaPedido,
          COALESCE(a.gastos_envio, 0)           AS gastoEnvio,
          COALESCE(a.tiempo_preparacion_min,0)  AS tiempoPreparacion
        FROM pedido p
        LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
        ORDER BY p.numero_pedido
    """;

        List<Pedido> list = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setNumeroPedido(rs.getInt("numeroPedido"));

                Cliente cli = new ClienteEstandar();
                cli.setEmail(rs.getString("emailCliente"));
                p.setCliente(cli);

                Articulo art = new Articulo();
                art.setCodigo(rs.getString("codigoArticulo"));
                p.setArticulo(art);

                p.setCantidad(rs.getInt("cantidad"));
                p.setFechaPedido(rs.getTimestamp("fechaPedido").toLocalDateTime());
                p.setGastoEnvio(rs.getDouble("gastoEnvio"));
                p.setTiempoPreparacion(rs.getInt("tiempoPreparacion"));

                list.add(p);
            }
        }
        return list;
    }

    public List<Pedido> obtenerPendientes(String emailCliente) throws SQLException {

        String base = """
        SELECT
            p.numero_pedido AS numeroPedido,
            p.cliente_email AS emailCliente,
            p.articulo_codigo AS codigoArticulo,
            p.unidades AS cantidad,
            p.fecha_hora AS fechaPedido,
            a.gastos_envio AS gastoEnvio,
            a.tiempo_preparacion_min AS tiempoPreparacion
        FROM pedido p
        JOIN articulo a ON a.codigo = p.articulo_codigo
        WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) < a.tiempo_preparacion_min
    """;

        String sql = (emailCliente == null) ? base : base + " AND p.cliente_email = ?";

        List<Pedido> list = new ArrayList<>();

        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (emailCliente != null) {
                ps.setString(1, emailCliente);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();

                    p.setNumeroPedido(rs.getInt("numeroPedido"));

                    Cliente cli = new ClienteEstandar();
                    cli.setEmail(rs.getString("emailCliente"));
                    p.setCliente(cli);

                    Articulo art = new Articulo();
                    art.setCodigo(rs.getString("codigoArticulo"));
                    p.setArticulo(art);

                    p.setCantidad(rs.getInt("cantidad"));

                    // convertir timestamp a LocalDateTime
                    p.setFechaPedido(rs.getTimestamp("fechaPedido").toLocalDateTime());

                    // leer gastoEnvio y tiempoPreparacion desde la tabla articulo
                    p.setGastoEnvio(rs.getDouble("gastoEnvio"));
                    p.setTiempoPreparacion(rs.getInt("tiempoPreparacion"));

                    list.add(p);
                }
            }
        }

        return list;
    }




    public List<Pedido> obtenerEnviados(String emailCliente) throws SQLException {
        String base = """
        SELECT
          p.numero_pedido            AS numeroPedido,
          p.cliente_email           AS emailCliente,
          p.articulo_codigo         AS codigoArticulo,
          p.unidades                AS cantidad,
          p.fecha_hora              AS fechaPedido,
          COALESCE(a.gastos_envio, 0)           AS gastoEnvio,
          COALESCE(a.tiempo_preparacion_min,0)  AS tiempoPreparacion
        FROM pedido p
        LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
        WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) >= COALESCE(a.tiempo_preparacion_min,0)
        ORDER BY p.numero_pedido
    """;

        String sql = (emailCliente == null)
                ? base
                : base.replace("ORDER BY p.numero_pedido", "AND p.cliente_email = ? ORDER BY p.numero_pedido");

        List<Pedido> list = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (emailCliente != null) {
                ps.setString(1, emailCliente);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setNumeroPedido(rs.getInt("numeroPedido"));

                    Cliente cli = new ClienteEstandar();
                    cli.setEmail(rs.getString("emailCliente"));
                    p.setCliente(cli);

                    Articulo art = new Articulo();
                    art.setCodigo(rs.getString("codigoArticulo"));
                    p.setArticulo(art);

                    p.setCantidad(rs.getInt("cantidad"));
                    p.setFechaPedido(rs.getTimestamp("fechaPedido").toLocalDateTime());
                    p.setGastoEnvio(rs.getDouble("gastoEnvio"));
                    p.setTiempoPreparacion(rs.getInt("tiempoPreparacion"));

                    list.add(p);
                }
            }
        }
        return list;
    }



    public List<Pedido> obtenerTodosFiltradosPorCliente(String emailCliente) throws SQLException {
    String sql = """
        SELECT
          p.numero_pedido            AS numeroPedido,
          p.cliente_email           AS emailCliente,
          p.articulo_codigo         AS codigoArticulo,
          p.unidades                AS cantidad,
          p.fecha_hora              AS fechaPedido,
          COALESCE(a.gastos_envio, 0)           AS gastoEnvio,
          COALESCE(a.tiempo_preparacion_min,0)  AS tiempoPreparacion
        FROM pedido p
        LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
        WHERE p.cliente_email = ?
        ORDER BY p.numero_pedido
    """;

    List<Pedido> list = new ArrayList<>();
    try (Connection c = ConexionBD.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setString(1, emailCliente);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setNumeroPedido(rs.getInt("numeroPedido"));

                Cliente cli = new ClienteEstandar();
                cli.setEmail(rs.getString("emailCliente"));
                p.setCliente(cli);

                Articulo art = new Articulo();
                art.setCodigo(rs.getString("codigoArticulo"));
                p.setArticulo(art);

                p.setCantidad(rs.getInt("cantidad"));
                p.setFechaPedido(rs.getTimestamp("fechaPedido").toLocalDateTime());
                p.setGastoEnvio(rs.getDouble("gastoEnvio"));
                p.setTiempoPreparacion(rs.getInt("tiempoPreparacion"));

                list.add(p);
            }
        }
    }
    return list;
}

    public List<Pedido> obtenerCompletados(String emailCliente) throws SQLException {
        String base = """
        SELECT
          p.numero_pedido            AS numeroPedido,
          p.cliente_email           AS emailCliente,
          p.articulo_codigo         AS codigoArticulo,
          p.unidades                AS cantidad,
          p.fecha_hora              AS fechaPedido,
          COALESCE(a.gastos_envio, 0)           AS gastoEnvio,
          COALESCE(a.tiempo_preparacion_min,0)  AS tiempoPreparacion
        FROM pedido p
        LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
        WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) >= COALESCE(a.tiempo_preparacion_min,0)
        ORDER BY p.numero_pedido
    """;

        String sql = (emailCliente == null) ? base : base.replace("ORDER BY p.numero_pedido", "AND p.cliente_email = ? ORDER BY p.numero_pedido");

        List<Pedido> list = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (emailCliente != null) ps.setString(1, emailCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setNumeroPedido(rs.getInt("numeroPedido"));

                    Cliente cli = new ClienteEstandar();
                    cli.setEmail(rs.getString("emailCliente"));
                    p.setCliente(cli);

                    Articulo art = new Articulo();
                    art.setCodigo(rs.getString("codigoArticulo"));
                    p.setArticulo(art);

                    p.setCantidad(rs.getInt("cantidad"));
                    p.setFechaPedido(rs.getTimestamp("fechaPedido").toLocalDateTime());
                    p.setGastoEnvio(rs.getDouble("gastoEnvio"));
                    p.setTiempoPreparacion(rs.getInt("tiempoPreparacion"));

                    list.add(p);
                }
            }
        }
        return list;
    }

}

