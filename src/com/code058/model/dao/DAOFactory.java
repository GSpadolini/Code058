
package com.code058.model.dao;

public interface DAOFactory {
    ArticuloDAO getArticuloDAO();
    ClienteDAO getClienteDAO();
    PedidoDAO getPedidoDAO();
}
