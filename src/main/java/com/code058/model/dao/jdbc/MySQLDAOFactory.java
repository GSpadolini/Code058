package com.code058.model.dao.jdbc;

import com.code058.model.dao.*;

public class MySQLDAOFactory implements DAOFactory {
    @Override
    public ArticuloDAO getArticuloDAO() {
        return new ArticuloDAOImpl();
    }

    @Override
    public ClienteDAO getClienteDAO() {
        return new ClienteDAOImpl();
    }

    @Override
    public PedidoDAO getPedidoDAO() {
        return new PedidoDAOImpl();
    }
}
