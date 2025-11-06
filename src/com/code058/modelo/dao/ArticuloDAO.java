package com.code058.modelo.dao;

import com.code058.model.Articulo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticuloDAO {
    // obtener todos
    public List<Articulo> obtenerTodos() throws SQLException {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min FROM articulo";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Articulo a = new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion_min")
                );
                lista.add(a);
            }
        }
        return lista;
    }

    // insertar
    public void insertar(Articulo a) throws SQLException {
        String sql = "INSERT INTO articulo (codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getCodigo());
            ps.setString(2, a.getDescripcion());
            ps.setDouble(3, a.getPrecioVenta());
            ps.setDouble(4, a.getGastoEnvio());
            ps.setInt(5, a.getTiempoPreparacionMin());
            ps.executeUpdate();
        }
    }

    // obtenerPorCodigo
    public Articulo obtenerPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min FROM articulo WHERE codigo = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Articulo(
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio_venta"),
                            rs.getDouble("gastos_envio"),
                            rs.getInt("tiempo_preparacion_min")
                    );
                }
                return null;
            }
        }
    }

    // existeCodigo
    public boolean existeCodigo(String codigo) throws SQLException {
        String sql = "SELECT 1 FROM articulo WHERE codigo = ? LIMIT 1";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
