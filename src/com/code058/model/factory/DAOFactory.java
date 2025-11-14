package com.code058.model.factory;

import com.code058.model.dao.ArticuloDAO;
import com.code058.model.dao.ClienteDAO;
import com.code058.model.dao.PedidoDAO;
import com.code058.model.dao.mysql.MySQLArticuloDAO;
import com.code058.model.dao.mysql.MySQLClienteDAO;
import com.code058.model.dao.mysql.MySQLPedidoDAO;

public class DAOFactory {
    /**
     * Devuelve una instancia de la interfaz ArticuloDAO.
     * @return ArticuloDAO (la implementación concreta es MySQLArticuloDAO)
     */
    public static ArticuloDAO getArticuloDAO() {
        return new MySQLArticuloDAO();
    }

    /**
     * Devuelve una instancia de la interfaz ClienteDAO.
     */
    public static ClienteDAO getClienteDAO() {
        return new MySQLClienteDAO();
    }

    /**
     * Devuelve una instancia de la interfaz PedidoDAO.
     */
    public static PedidoDAO getPedidoDAO() {
        return new MySQLPedidoDAO();
    }
}
