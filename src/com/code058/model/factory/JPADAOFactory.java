package com.code058.model.factory;

import com.code058.model.dao.ArticuloDAO;
import com.code058.model.dao.ClienteDAO;
import com.code058.model.dao.PedidoDAO;

import com.code058.model.dao.JPAArticuloDAO; // Importamos las nuevas implementaciones JPA
import com.code058.model.dao.JPAClienteDAO;
import com.code058.model.dao.JPAPedidoDAO;

public class JPADAOFactory implements DAOFactory{

    @Override
    public ArticuloDAO getArticuloDAO() throws Exception {
        return new JPAArticuloDAO();
    }

    @Override
    public ClienteDAO getClienteDAO() throws Exception{
        return new JPAClienteDAO(); // ¡Devuelve la implementación JPA!
    }

    @Override
    public PedidoDAO getPedidoDAO() throws Exception{
        return new JPAPedidoDAO(); // ¡Devuelve la implementación JPA!
    }
}
