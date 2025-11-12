package com.code058.model.dao.jdbc;

import com.code058.model.Pedido;
import com.code058.model.dao.PedidoDAO;
import com.code058.model.dao.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public void insertar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (cliente_email, articulo_codigo, unidades, fecha_hora, gasto_envio, tiempo_preparacion_min) VALUES (?, ?, ?, ?, ?, ?)";


        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pedido.getClienteEmail());
            ps.setString(2, pedido.getArticulo().getCodigo());
            ps.setInt(3, pedido.getCantidad());
            ps.setTimestamp(4, Timestamp.valueOf(pedido.getFechaPedido()));
            ps.setDouble(5, pedido.getGastoEnvio());
            ps.setInt(6, pedido.getTiempoPreparacion());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public List<Pedido> listarTodos() throws SQLException {
        String sql = "SELECT * FROM pedido";
        Connection conn = DBConnection.getConnection();
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setNumeroPedido(rs.getInt("numero_pedido"));
                pedido.setCantidad(rs.getInt("unidades"));
                pedido.setFechaPedido(rs.getTimestamp("fecha_pedido").toLocalDateTime());
                pedido.setGastoEnvio(rs.getDouble("gasto_envio"));
                pedido.setTiempoPreparacion(rs.getInt("tiempo_preparacion_min"));

                pedidos.add(pedido);
            }
        }

        return pedidos;
    }

    @Override
    public void eliminar(int numeroPedido) throws SQLException {
        String sql = "DELETE FROM pedido WHERE numero_pedido = ?";
        Connection conn = DBConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numeroPedido);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
