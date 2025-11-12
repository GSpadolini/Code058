package com.code058.model.dao;

import com.code058.model.Cliente;
import java.sql.SQLException;
import java.util.Map;

public interface ClienteDAO {
    void insertar(Cliente cliente) throws SQLException;
    Map<String, Cliente> listarTodos() throws SQLException;
    boolean existeEmail(String email) throws SQLException;
}
