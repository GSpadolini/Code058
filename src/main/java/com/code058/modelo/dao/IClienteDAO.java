package com.code058.modelo.dao;

import com.code058.model.Cliente;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IClienteDAO {

    // ===== Métodos estándar =====
    void insertar(Cliente c) throws SQLException;
    boolean existeEmail(String email) throws SQLException;
    Cliente obtenerPorEmail(String email) throws SQLException;
    List<Cliente> obtenerTodos() throws SQLException;

    // ===== Métodos transaccionales =====
    void insertar(Connection conn, Cliente c) throws SQLException;
    boolean existeEmail(Connection conn, String email) throws SQLException;
    Cliente obtenerPorEmail(Connection conn, String email) throws SQLException;
    List<Cliente> obtenerTodos(Connection conn) throws SQLException;
}

