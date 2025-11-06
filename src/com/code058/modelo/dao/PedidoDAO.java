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
        // Comprobamos cancelable con TIMESTAMPDIFF (evitas traer todo y calculas en SQL)
        String q = """
            SELECT TIMESTAMPDIFF(MINUTE, fechaPedido, NOW()) AS minutos, tiempoPreparacion
            FROM pedido
            WHERE numeroPedido = ?
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
                    try (PreparedStatement del = c.prepareStatement("DELETE FROM pedido WHERE numeroPedido = ?")) {
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
        // Carga básica (sin hidratar Cliente/Articulo completos)
        List<Pedido> list = new ArrayList<>();
        String sql = "SELECT numeroPedido, emailCliente, codigoArticulo, cantidad, fechaPedido, gastoEnvio, tiempoPreparacion FROM pedido";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setNumeroPedido(rs.getInt("numeroPedido"));
                // si necesitas el objeto completo, tendrás que cargar Cliente/Articulo con sus DAO
                Cliente cli = new ClienteEstandar(); // placeholder
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
        // pendiente = cancelable -> TIMESTAMPDIFF < tiempoPreparacion
        String base = """
            SELECT numeroPedido, emailCliente, codigoArticulo, cantidad, fechaPedido, gastoEnvio, tiempoPreparacion
            FROM pedido
            WHERE TIMESTAMPDIFF(MINUTE, fechaPedido, NOW()) < tiempoPreparacion
        """;
        String sql = (emailCliente == null)
                ? base
                : base + " AND emailCliente = ?";

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

    public List<Pedido> obtenerEnviados(String emailCliente) throws SQLException {
        String base = """
            SELECT numeroPedido, emailCliente, codigoArticulo, cantidad, fechaPedido, gastoEnvio, tiempoPreparacion
            FROM pedido
            WHERE TIMESTAMPDIFF(MINUTE, fechaPedido, NOW()) >= tiempoPreparacion
        """;
        String sql = (emailCliente == null)
                ? base
                : base + " AND emailCliente = ?";

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

