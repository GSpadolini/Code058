package com.code058.modelo.dao;

import com.code058.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements IPedidoDAO {

    // =========================
    // MÉTODOS ESTÁNDAR
    // =========================

    @Override
    public int insertar(Pedido p) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return insertar(conn, p);
        }
    }

    @Override
    public boolean eliminarSiCancelable(int numeroPedido) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return eliminarSiCancelable(conn, numeroPedido);
        }
    }

    @Override
    public List<Pedido> obtenerTodosBasico() throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return obtenerTodosBasico(conn);
        }
    }

    @Override
    public List<Pedido> obtenerPendientes(String emailCliente) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return obtenerPendientes(conn, emailCliente);
        }
    }

    @Override
    public List<Pedido> obtenerEnviados(String emailCliente) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return obtenerEnviados(conn, emailCliente);
        }
    }

    @Override
    public List<Pedido> obtenerTodosFiltradosPorCliente(String emailCliente) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return obtenerTodosFiltradosPorCliente(conn, emailCliente);
        }
    }

    @Override
    public List<Pedido> obtenerCompletados(String emailCliente) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return obtenerCompletados(conn, emailCliente);
        }
    }

    // =========================
    // MÉTODOS TRANSACCIONALES
    // =========================

    @Override
    public int insertar(Connection conn, Pedido p) throws SQLException {
        String sql = "INSERT INTO pedido (cliente_email, articulo_codigo, unidades, fecha_hora) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getCliente().getEmail());
            ps.setString(2, p.getArticulo().getCodigo());
            ps.setInt(3, p.getCantidad());
            ps.setTimestamp(4, Timestamp.valueOf(p.getFechaPedido()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    p.setNumeroPedido(newId);
                    return newId;
                }
                return -1;
            }
        }
    }

    @Override
    public boolean eliminarSiCancelable(Connection conn, int numeroPedido) throws SQLException {

        String q = """
            SELECT TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) AS minutos,
                   COALESCE(a.tiempo_preparacion_min, 0) AS tiempoPreparacion
            FROM pedido p
            LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
            WHERE p.numero_pedido = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, numeroPedido);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) return false;

                int minutos = rs.getInt("minutos");
                int prep = rs.getInt("tiempoPreparacion");

                if (minutos < prep) {
                    try (PreparedStatement del = conn.prepareStatement("DELETE FROM pedido WHERE numero_pedido = ?")) {
                        del.setInt(1, numeroPedido);
                        del.executeUpdate();
                        return true;
                    }
                }
                return false;
            }
        }
    }

    // MÉTODOS DE LISTADO con Connection


    @Override
    public List<Pedido> obtenerTodosBasico(Connection conn) throws SQLException {
        String sql = """
            SELECT
              p.numero_pedido AS numeroPedido,
              p.cliente_email AS emailCliente,
              p.articulo_codigo AS codigoArticulo,
              p.unidades AS cantidad,
              p.fecha_hora AS fechaPedido,
              COALESCE(a.gastos_envio, 0) AS gastoEnvio,
              COALESCE(a.tiempo_preparacion_min,0) AS tiempoPreparacion
            FROM pedido p
            LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
            ORDER BY p.numero_pedido
        """;

        List<Pedido> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToPedido(rs));
            }
        }

        return list;
    }

    @Override
    public List<Pedido> obtenerPendientes(Connection conn, String emailCliente) throws SQLException {

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

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            if (emailCliente != null) ps.setString(1, emailCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPedido(rs));
                }
            }
        }

        return list;
    }

    @Override
    public List<Pedido> obtenerEnviados(Connection conn, String emailCliente) throws SQLException {

        String base = """
            SELECT
              p.numero_pedido AS numeroPedido,
              p.cliente_email AS emailCliente,
              p.articulo_codigo AS codigoArticulo,
              p.unidades AS cantidad,
              p.fecha_hora AS fechaPedido,
              COALESCE(a.gastos_envio, 0) AS gastoEnvio,
              COALESCE(a.tiempo_preparacion_min,0) AS tiempoPreparacion
            FROM pedido p
            LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
            WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) >= COALESCE(a.tiempo_preparacion_min,0)
            ORDER BY p.numero_pedido
        """;

        String sql = (emailCliente == null)
                ? base
                : base.replace("ORDER BY p.numero_pedido", "AND p.cliente_email = ? ORDER BY p.numero_pedido");

        List<Pedido> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            if (emailCliente != null) ps.setString(1, emailCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPedido(rs));
                }
            }
        }

        return list;
    }

    @Override
    public List<Pedido> obtenerTodosFiltradosPorCliente(Connection conn, String emailCliente) throws SQLException {

        String sql = """
            SELECT
              p.numero_pedido AS numeroPedido,
              p.cliente_email AS emailCliente,
              p.articulo_codigo AS codigoArticulo,
              p.unidades AS cantidad,
              p.fecha_hora AS fechaPedido,
              COALESCE(a.gastos_envio, 0) AS gastoEnvio,
              COALESCE(a.tiempo_preparacion_min,0) AS tiempoPreparacion
            FROM pedido p
            LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
            WHERE p.cliente_email = ?
            ORDER BY p.numero_pedido
        """;

        List<Pedido> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emailCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToPedido(rs));
            }
        }

        return list;
    }

    @Override
    public List<Pedido> obtenerCompletados(Connection conn, String emailCliente) throws SQLException {

        String base = """
            SELECT
              p.numero_pedido AS numeroPedido,
              p.cliente_email AS emailCliente,
              p.articulo_codigo AS codigoArticulo,
              p.unidades AS cantidad,
              p.fecha_hora AS fechaPedido,
              COALESCE(a.gastos_envio, 0) AS gastoEnvio,
              COALESCE(a.tiempo_preparacion_min,0) AS tiempoPreparacion
            FROM pedido p
            LEFT JOIN articulo a ON a.codigo = p.articulo_codigo
            WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) >= COALESCE(a.tiempo_preparacion_min,0)
            ORDER BY p.numero_pedido
        """;

        String sql = (emailCliente == null)
                ? base
                : base.replace("ORDER BY p.numero_pedido", "AND p.cliente_email = ? ORDER BY p.numero_pedido");

        List<Pedido> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            if (emailCliente != null) ps.setString(1, emailCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToPedido(rs));
            }
        }

        return list;
    }


    // =========================
    // MÉTODO AUXILIAR: mapear ResultSet → Pedido
    // =========================

    private Pedido mapRowToPedido(ResultSet rs) throws SQLException {
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
        p.setGastoEnvio(rs.getBigDecimal("gastoEnvio"));
        p.setTiempoPreparacion(rs.getInt("tiempoPreparacion"));

        return p;
    }

    //Stored Procedures
    public int insertarUsandoSP(Pedido p) throws SQLException {
        String call = "{CALL sp_insert_pedido(?, ?, ?, ?, ?)}";
        try (Connection c = ConexionBD.getConnection();
             CallableStatement cs = c.prepareCall(call)) {
            cs.setString(1, p.getCliente().getEmail());
            cs.setString(2, p.getArticulo().getCodigo());
            cs.setInt(3, p.getCantidad());
            cs.setTimestamp(4, Timestamp.valueOf(p.getFechaPedido()));
            cs.registerOutParameter(5, java.sql.Types.INTEGER);
            cs.execute();
            int newId = cs.getInt(5);
            p.setNumeroPedido(newId);
            return newId;
        }
    }

}
