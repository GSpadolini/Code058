package com.code058.modelo.dao;

import com.code058.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

public class ClienteDAO {
    public void insertar(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (email, nombre, domicilio, nif, tipo_cliente, cuota_anual, descuento_envio) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getEmail());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDomicilio());
            ps.setString(4, c.getNif());
            if (c instanceof ClientePremium premium) {
                ps.setString(5, "PREMIUM");
                ps.setBigDecimal(6, BigDecimal.valueOf(premium.getCuotaAnual()));
                ps.setBigDecimal(7, BigDecimal.valueOf(premium.descuentoEnvio())); // si tienes método
            } else {
                ps.setString(5, "ESTANDAR");
                ps.setNull(6, Types.DECIMAL);
                ps.setNull(7, Types.DECIMAL);
            }
            ps.executeUpdate();
        }
    }

    // existeEmail
    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM cliente WHERE email = ? LIMIT 1";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }


    // obtenerPorEmail
    public Cliente obtenerPorEmail(String email) throws SQLException {
        String sql = "SELECT email, nombre, domicilio, nif, tipo_cliente, cuota_anual, descuento_envio FROM cliente WHERE email = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo_cliente");
                    if ("PREMIUM".equalsIgnoreCase(tipo)) {
                        ClientePremium p = new ClientePremium(
                                rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email")
                        );
                        if (rs.getObject("cuota_anual") != null) p.setCuotaAnual(rs.getDouble("cuota_anual"));
                        if (rs.getObject("descuento_envio") != null) {
                            // si tu ClientePremium tiene setDescuentoEnvio o similar, asigna
                        }
                        return p;
                    } else {
                        return new ClienteEstandar(
                                rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email")
                        );
                    }
                }
                return null;
            }
        }
    }

    public List<Cliente> obtenerTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT email, nombre, domicilio, nif, tipo_cliente, cuota_anual, descuento_envio FROM cliente";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("tipo_cliente");
                String email = rs.getString("email");
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String nif = rs.getString("nif");

                if ("PREMIUM".equalsIgnoreCase(tipo)) {
                    ClientePremium cp = new ClientePremium(nombre, domicilio, nif, email);
                    // Si la columna cuota_anual no es NULL, la asignamos
                    if (rs.getObject("cuota_anual") != null) {
                        cp.setCuotaAnual(rs.getDouble("cuota_anual"));
                    }
                    // Si tienes método para descuento_envio, asignalo; si no, lo ignoramos
                    // if (rs.getObject("descuento_envio") != null) cp.setDescuentoEnvio(rs.getDouble("descuento_envio"));
                    lista.add(cp);
                } else {
                    ClienteEstandar ce = new ClienteEstandar(nombre, domicilio, nif, email);
                    lista.add(ce);
                }
            }
        }
        return lista;
    }

}
