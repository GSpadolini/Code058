package com.code058.modelo.dao;

public class DAOFactory {

    public static ArticuloDAO getArticuloDAO() {
        return new ArticuloDAO();
    }

    public static ClienteDAO getClienteDAO() {
        return new ClienteDAO();
    }

    public static PedidoDAO getPedidoDAO() {
        return new PedidoDAO();
    }
}
