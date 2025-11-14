package com.code058.model;

import com.code058.exceptions.DuplicadosException;
import com.code058.exceptions.PedidoNoCancelableException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code058.model.dao.ArticuloDAO;
import com.code058.model.dao.ClienteDAO;
import com.code058.model.dao.PedidoDAO;
import com.code058.model.factory.DAOFactory;

public class GestorDeDatos {

    private ClienteDAO clienteDAO;
    private ArticuloDAO articuloDAO;
    private PedidoDAO pedidoDAO;



    public GestorDeDatos() {

        this.clienteDAO = DAOFactory.getClienteDAO();
        this.articuloDAO = DAOFactory.getArticuloDAO();
        this.pedidoDAO = DAOFactory.getPedidoDAO();
        // Inicialización de las estructuras de datos dinámicas



    }

    //Gestión artículos
    public void anadirArticulo(Articulo articulo) throws Exception {
        try {
            // La BBDD (DAO) ahora maneja la lógica de inserción y la verificación de duplicados (PK).
            articuloDAO.insertar(articulo);
        } catch (Exception e) {
            // Capturamos el error del DAO. Si falla por PK duplicada, el mensaje SQL lo indicará.
            throw new Exception("Error al añadir artículo en la BBDD. Causa: " + e.getMessage());
        }
    }

    public Articulo getArticulo(String codigo) throws Exception {
        return articuloDAO.obtenerPorCodigo(codigo);
    }
    public java.util.List<Articulo> getArticulos() throws Exception {
        return articuloDAO.obtenerTodos();
    }


    //Gestión cliente
    public void anadirCliente(Cliente cliente) throws Exception {
        try {
            clienteDAO.insertar(cliente); // Llama al metodo transaccional del DAO
        } catch (Exception e) {
            // El DAO ya maneja las excepciones SQL, pero las relanzamos
            // para que el Controlador pueda informar a la Vista.
            throw new Exception("Error al añadir cliente en la BBDD: " + e.getMessage());
        }
    }

    public Cliente getCliente(String email) throws Exception {
        try {
            // Llama al metodo obtenerPorEmail del DAO
            return clienteDAO.obtenerPorEmail(email);
        } catch (Exception e) {
            throw new Exception("Error al buscar cliente por email en la BBDD: " + e.getMessage());
        }
    }

    public List<Cliente> getClientes() throws Exception {
        try {
            // Llama al metodo obtenerPorEmail del DAO
            return clienteDAO.obtenerTodos();
        } catch (Exception e) {
            throw new Exception("Error al buscar clientes en la BBDD: " + e.getMessage());
        }
    }

    public List<Cliente> getClientesEstandar() throws Exception {
        try {
            // Llama al DAO, que ya hace el SELECT WHERE tipo_cliente = 'ESTANDAR'
            return clienteDAO.obtenerEstandar();
        } catch (Exception e) {
            throw new Exception("Error al obtener clientes Estándar: " + e.getMessage());
        }
    }
    public List<Cliente> getClientesPremium() throws Exception {
        try {
            // Llama al DAO, que ya hace el SELECT WHERE tipo_cliente = 'PREMIUM'
            return clienteDAO.obtenerPremium();
        } catch (Exception e) {
            throw new Exception("Error al obtener clientes Premium: " + e.getMessage());
        }
    }


    //Gestión pedidos
    public void crearPedido(Pedido pedido) throws Exception {
        try {
            pedidoDAO.insertar(pedido);
        } catch (Exception e) {
            throw new Exception("Error al crear el pedido en la BBDD. Causa: " + e.getMessage());
        }
    }

//    private int generarNumeroPedido(){
//        if (pedidos.isEmpty()){
//            return 1;
//        }
//        Pedido ultimoPedido = pedidos.get(pedidos.size() - 1);
//        return ultimoPedido.getNumeroPedido() + 1;
//    }

    public String eliminarPedido(int numPedido) throws Exception {
        try {
            pedidoDAO.eliminar(numPedido);
            return "PEDIDO_ELIMINADO";
        } catch (PedidoNoCancelableException e) {
            throw e;
        } catch (Exception e) {
            // Capturamos cualquier error de SQL/No encontrado.
            if (e.getMessage().contains("no encontrado") || e.getMessage().contains("no existe")) {
                return "PEDIDO_NO_ENCONTRADO";
            }
            throw new Exception("Error al eliminar pedido en la BBDD. Causa: " + e.getMessage());
        }
    }

    public List<Pedido> getPedidosPendientes() throws Exception {
        return pedidoDAO.obtenerPendientes(null); // Asume que el DAO filtra por NULL si no hay email
    }

    public List<Pedido> getPedidosPendientes(String emailCliente) throws Exception {
        return pedidoDAO.obtenerPendientes(emailCliente);
    }

    public List<Pedido> getPedidosEviados() throws Exception {
        return pedidoDAO.obtenerEnviados(null);
    }

    public List<Pedido> getPedidosEviados(String emailCliente) throws Exception {
        return pedidoDAO.obtenerEnviados(emailCliente);
    }

}
