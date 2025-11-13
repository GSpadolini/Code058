
package com.code058.model.dao.jdbc;

import com.code058.model.Articulo;
import com.code058.model.dao.ArticuloDAO;
import com.code058.model.dao.util.ConexionBBDD;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLArticuloDAO implements ArticuloDAO {

    public void insertar(Articulo articulo) throws SQLException {
        String sql = "INSERT INTO articulo (codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min) " +
                "VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConexionBBDD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, articulo.getCodigo());
            ps.setString(2, articulo.getDescripcion());
            ps.setDouble(3, articulo.getPrecioVenta());
            ps.setDouble(4, articulo.getGastoEnvio());
            ps.setInt(5, articulo.getTiempoPreparacionMin());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public Map<String, Articulo> listarTodos() throws SQLException {
        Map<String, Articulo> lista = new HashMap<>();
        String sql = "SELECT * FROM articulo";
        Connection conn = ConexionBBDD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Articulo a = new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion_min")
                );
                lista.put(a.getCodigo(), a);
            }
        }
        return lista;
    }

    public boolean existeCodigo(String codigo) throws SQLException {
        String sql = "SELECT 1 FROM articulo WHERE codigo = ?";
        Connection conn = ConexionBBDD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}

