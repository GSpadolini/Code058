package com.code058.service;

import com.code058.modelo.dao.*;
import com.code058.model.Pedido;
import java.sql.Connection;

public class PedidoService {
    private final IPedidoDAO pedidoDAO;
    private final IClienteDAO clienteDAO;
    private final IArticuloDAO articuloDAO;

    public PedidoService(IPedidoDAO pedidoDAO, IClienteDAO clienteDAO, IArticuloDAO articuloDAO) {
        this.pedidoDAO = pedidoDAO;
        this.clienteDAO = clienteDAO;
        this.articuloDAO = articuloDAO;
}

    public int crearPedidoTransactional(Pedido pedido) throws Exception {
        try (Connection conn = ConexionBD.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // Ejemplo: comprobar existencia cliente y artículo usando las versiones con Connection
                if (!clienteDAO.existeEmail(conn, pedido.getCliente().getEmail())) {
                    throw new Exception("Cliente no existe");
                }
                if (!articuloDAO.existeCodigo(conn, pedido.getArticulo().getCodigo())) {
                    throw new Exception("Artículo no existe");
                }

                int id = pedidoDAO.insertar(conn, pedido); // inserta y setea numeroPedido
                // otros pasos relacionados...
                conn.commit();
                return id;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}

