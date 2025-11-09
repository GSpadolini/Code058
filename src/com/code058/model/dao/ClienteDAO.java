package com.code058.model.dao;

import com.code058.model.Cliente;

import java.util.List;

public interface ClienteDAO {

    // Create
    public void insertar(Cliente cliente) throws Exception;

    // Read (todos)
    public List<Cliente> obtenerTodos() throws Exception;

    // Métodos específicos del menú (para Cliente)
    public List<Cliente> obtenerEstandar() throws Exception;
    public List<Cliente> obtenerPremium() throws Exception;

}
