package com.code058.model.dao.jdbc;

import com.code058.model.Cliente;
import com.code058.model.ClienteEstandar;
import com.code058.model.ClientePremium;
import com.code058.model.dao.ClienteDAO;
import com.code058.model.dao.util.DBConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (email, nombre, domicilio, nif, tipo_cliente, cuota_anual, descuento_envio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getEmail());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDomicilio());
            ps.setString(4, cliente.getNif());
            if (cliente instanceof ClienteEstandar) {
                ps.setString(5, "ESTANDAR");
                ps.setNull(6, Types.DECIMAL);
                ps.setNull(7, Types.DECIMAL);
            } else if (cliente instanceof ClientePremium) {
                ClientePremium c = (ClientePremium) cliente;
                ps.setString(5, "PREMIUM");
                ps.setDouble(6, c.getCuotaAnual());
                ps.setDouble(7, c.descuentoEnvio());
            }
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public Map<String, Cliente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM cliente";
        Connection conn = DBConnection.getConnection();
        Map<String, Cliente> clientes = new HashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tipo = rs.getString("tipo_cliente");
                Cliente c;
                if ("PREMIUM".equalsIgnoreCase(tipo)) {
                    c = new ClientePremium(
                            rs.getString("email"),
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getDouble("cuota_anual"),
                            rs.getDouble("descuento_envio")
                    );
                } else {
                    c = new ClienteEstandar(
                            rs.getString("email"),
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif")
                    );
                }
                clientes.put(c.getEmail(), c);
            }
        }
        return clientes;
    }

    @Override
    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM cliente WHERE email = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
