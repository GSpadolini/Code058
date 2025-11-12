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
        String sql = "INSERT INTO pedido (cliente_email, articulo_codigo, unidades, fecha_hora) " +
                "VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pedido.getClienteEmail());
            ps.setString(2, pedido.getArticuloCodigo());
            ps.setInt(3, pedido.getUnidades());
            ps.setTimestamp(4, Timestamp.valueOf(pedido.getFechaHora()));
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
                Pedido p = new Pedido(
                        rs.getInt("numero_pedido"),
                        rs.getString("cliente_email"),
                        rs.getString("articulo_codigo"),
                        rs.getInt("unidades"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime()
                );
                pedidos.add(p);
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
