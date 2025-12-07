package com.code058.modelo.dao;

import com.code058.model.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO {

    // MÉTODOS ESTÁNDAR (AUTO-CONEXIÓN)


    @Override
    public void insertar(Cliente c) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            insertar(conn, c);
        }
    }

    @Override
    public boolean existeEmail(String email) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return existeEmail(conn, email);
        }
    }

    @Override
    public Cliente obtenerPorEmail(String email) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return obtenerPorEmail(conn, email);
        }
    }

    @Override
    public List<Cliente> obtenerTodos() throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            return obtenerTodos(conn);
        }
    }

    // =========================
    // MÉTODOS TRANSACCIONALES (CONNECTION EXTERNA)
    // =========================

    @Override
    public void insertar(Connection conn, Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (email, nombre, domicilio, nif, tipo_cliente, cuota_anual, descuento_envio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getEmail());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDomicilio());
            ps.setString(4, c.getNif());

            if (c instanceof ClientePremium premium) {
                ps.setString(5, "PREMIUM");
                ps.setBigDecimal(6, premium.getCuotaAnual());
                ps.setBigDecimal(7, premium.getDescuentoEnvio());
            } else {
                ps.setString(5, "ESTANDAR");
                ps.setNull(6, Types.DECIMAL);
                ps.setNull(7, Types.DECIMAL);
            }

            ps.executeUpdate();
        }
    }

    @Override
    public boolean existeEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT 1 FROM cliente WHERE email = ? LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Cliente obtenerPorEmail(Connection conn, String email) throws SQLException {
        String sql = """
            SELECT email, nombre, domicilio, nif, tipo_cliente, cuota_anual, descuento_envio 
            FROM cliente WHERE email = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String tipo = rs.getString("tipo_cliente");

                if ("PREMIUM".equalsIgnoreCase(tipo)) {
                    ClientePremium p = new ClientePremium(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );

                    BigDecimal cuota = rs.getBigDecimal("cuota_anual");
                    if (cuota != null) {
                        p.setCuotaAnual(cuota);
                    }

                    BigDecimal desc = rs.getBigDecimal("descuento_envio");
                    if (desc != null) {
                        p.setDescuentoEnvio(desc);
                    }

                    return p;
                }

                return new ClienteEstandar(
                        rs.getString("nombre"),
                        rs.getString("domicilio"),
                        rs.getString("nif"),
                        rs.getString("email")
                );
            }
        }
    }

    @Override
    public List<Cliente> obtenerTodos(Connection conn) throws SQLException {
        List<Cliente> lista = new ArrayList<>();

        String sql = """
            SELECT email, nombre, domicilio, nif, tipo_cliente, cuota_anual, descuento_envio 
            FROM cliente
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("tipo_cliente");

                if ("PREMIUM".equalsIgnoreCase(tipo)) {
                    ClientePremium cp = new ClientePremium(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                    BigDecimal cuota = rs.getBigDecimal("cuota_anual");
                    if (cuota != null) cp.setCuotaAnual(cuota);

                    BigDecimal desc = rs.getBigDecimal("descuento_envio");
                    if (desc != null) cp.setDescuentoEnvio(desc);

                    lista.add(cp);
                } else {
                    ClienteEstandar ce = new ClienteEstandar(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                    lista.add(ce);
                }
            }
        }

        return lista;
    }
}
