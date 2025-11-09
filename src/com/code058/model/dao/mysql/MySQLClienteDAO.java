package com.code058.model.dao.mysql;

import com.code058.model.Cliente;
import com.code058.model.ClienteEstandar;
import com.code058.model.ClientePremium;
import com.code058.model.dao.ClienteDAO;
import com.code058.model.util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class MySQLClienteDAO implements ClienteDAO {

    private static final String INSERT_SQL =
            "INSERT INTO cliente (email, nombre, domicilio, nif, tipo_cliente, cuota_anual) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";



    private static final String SELECT_ALL_SQL =
            "SELECT * FROM cliente";

    private static final String SELECT_ESTANDAR_SQL =
            "SELECT * FROM cliente WHERE tipo_cliente = 'ESTANDAR'";

    private static final String SELECT_PREMIUM_SQL =
            "SELECT * FROM cliente WHERE tipo_cliente = 'PREMIUM'";

    private Cliente mapearCliente(ResultSet rs) throws SQLException {

        String tipo = rs.getString("tipo_cliente");

        // Lee los campos base
        String email = rs.getString("email");
        String nombre = rs.getString("nombre");
        String domicilio = rs.getString("domicilio");
        String nif = rs.getString("nif");

        if (tipo.equalsIgnoreCase("PREMIUM")) {
            // Para ClientePremium:
            double cuotaAnual = rs.getDouble("cuota_anual");

            // CORRECCIÓN: El descuentoEnvio (0.2) ya no se lee de la BBDD.
            // Se asume que el constructor de ClientePremium o su lógica POO le asigna el valor fijo (0.2).
            // Si el constructor requiere el valor, deberás pasárselo de forma fija:

            // Asegúrate de que el constructor de ClientePremium acepte este valor
            return new ClientePremium(email, nombre, domicilio, nif, cuotaAnual);

        } else { // ESTANDAR
            // Asegúrate de que ClienteEstandar no requiere los campos premium
            return new ClienteEstandar(email, nombre, domicilio, nif);
        }
    }

    @Override
    public void insertar(Cliente c) throws Exception {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            // 1. Mapeo de campos base
            ps.setString(1, c.getEmail());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDomicilio());
            ps.setString(4, c.getNif());

            // 2. Mapeo de jerarquía y campos opcionales (cuota_anual)
            if (c instanceof ClientePremium) {
                ClientePremium cp = (ClientePremium) c;

                ps.setString(5, "PREMIUM");
                ps.setDouble(6, cp.getCuotaAnual());



            } else if (c instanceof ClienteEstandar) {

                ps.setString(5, "ESTANDAR");
                ps.setDouble(6, 0.0);


            } else {
                throw new Exception("Tipo de cliente desconocido: no se puede insertar.");
            }

            // 3. Ejecutar la sentencia DML
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Error al insertar cliente: 0 filas afectadas.");
            }
        } catch (SQLException e) {
            throw new Exception("Error DAO al insertar cliente en la BBDD: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> obtenerTodos() throws Exception {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Mapeamos y añadimos a la lista en cada iteración
                Cliente cliente = mapearCliente(rs);
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            // Mejorar la excepción para que use el metodo getMessage()
            throw new Exception("Error DAO al obtener todos los clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    @Override
    public List<Cliente> obtenerEstandar() throws Exception {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ESTANDAR_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = mapearCliente(rs);
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            throw new Exception("Error DAO al obtener clientes estándar: " + e.getMessage(), e);
        }
        return clientes;
    }

    @Override
    public List<Cliente> obtenerPremium() throws Exception {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_PREMIUM_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = mapearCliente(rs);
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            throw new Exception("Error DAO al obtener clientes premium: " + e.getMessage(), e);
        }
        return clientes;
    }

}
