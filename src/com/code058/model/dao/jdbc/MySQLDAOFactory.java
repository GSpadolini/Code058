
package com.code058.model.dao.jdbc;

import com.code058.model.dao.*;

public class MySQLDAOFactory implements DAOFactory {
    @Override
    public ArticuloDAO getArticuloDAO() {
        return new MySQLArticuloDAO();
    }

    @Override
    public ClienteDAO getClienteDAO() {
        return new MySQLClienteDAO();
    }

    @Override
    public PedidoDAO getPedidoDAO() {
        return new MySQLPedidoDAO();
    }
}
