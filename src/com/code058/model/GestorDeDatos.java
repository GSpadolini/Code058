package com.code058.model;

import com.code058.modelo.dao.DAOFactory;
import com.code058.modelo.dao.ArticuloDAO;
import com.code058.modelo.dao.ClienteDAO;
import com.code058.modelo.dao.PedidoDAO;
import com.code058.exceptions.DuplicadosException;
import com.code058.exceptions.PedidoNoCancelableException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class GestorDeDatos {

    private final ArticuloDAO articuloDAO;
    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;

    public GestorDeDatos() {
        this.articuloDAO = DAOFactory.getArticuloDAO();
        this.clienteDAO  = DAOFactory.getClienteDAO();
        this.pedidoDAO   = DAOFactory.getPedidoDAO();
    }

    // ==== Artículos ====
    public void anadirArticulo(Articulo articulo) throws DuplicadosException {
        try {
            if (articuloDAO.existeCodigo(articulo.getCodigo())) {
                throw new DuplicadosException("Error de negocio: El artículo con código " + articulo.getCodigo() + " ya existe.");
            }
            articuloDAO.insertar(articulo);
        } catch (SQLException e) {
            // si quieres mapear códigos SQL específicos a errores de negocio, hazlo aquí
            throw new RuntimeException("Error persistiendo artículo", e);
        }
    }

    public List<Articulo> getArticulos() {
        try {
            return articuloDAO.obtenerTodos();
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo artículos", e);
        }
    }

    // ==== Clientes ====
    public void anadirCliente(Cliente cliente) throws DuplicadosException {
        try {
            if (clienteDAO.existeEmail(cliente.getEmail())) {
                throw new DuplicadosException("El cliente con el email " + cliente.getEmail() + " ya existe");
            }
            clienteDAO.insertar(cliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error persistiendo cliente", e);
        }
    }

    public List<Cliente> getClientes() {
        try {
            return clienteDAO.obtenerTodos();
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo clientes", e);
        }
    }

    public List<Cliente> getClientesEstandar() {
        return getClientes().stream().filter(c -> !(c instanceof ClientePremium)).toList();
    }

    public List<Cliente> getClientesPremium() {
        return getClientes().stream().filter(c -> (c instanceof ClientePremium)).toList();
    }

    // ==== Pedidos ====
    public void crearPedido(Pedido pedido) {
        try {
            // Si tu UI creaba el pedido con fecha/gasto/tiempo, mantenlo:
            if (pedido.getFechaPedido() == null) pedido.setFechaPedido(LocalDateTime.now());
            // numeroPedido lo pone el AUTO_INCREMENT y lo recuperamos en el DAO
            pedidoDAO.insertar(pedido);
        } catch (SQLException e) {
            throw new RuntimeException("Error creando pedido", e);
        }
    }

    public String eliminarPedido(int numPedido) throws PedidoNoCancelableException {
        try {
            boolean eliminado = pedidoDAO.eliminarSiCancelable(numPedido);
            if (eliminado) return "PEDIDO_ELIMINADO";
            // si no se eliminó, o no existe, o ya no es cancelable
            // distinguimos: si no existe devolvemos "no encontrado", si existe pero no cancelable lanzamos excepción
            // para simplificar, lanzamos la excepción si no se eliminó por cancelación
            throw new PedidoNoCancelableException("El pedido " + numPedido + " no puede cancelarse (ya se ha enviado o no existe)");
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando pedido", e);
        }
    }

    public List<Pedido> getPedidos() {
        try {
            return pedidoDAO.obtenerTodosBasico();
        } catch (SQLException e) {
            throw new RuntimeException("Error leyendo pedidos", e);
        }
    }

    public List<Pedido> getPedidosPendientes(String emailCliente) {
        try {
            return pedidoDAO.obtenerPendientes(emailCliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error leyendo pedidos pendientes", e);
        }
    }

    public List<Pedido> getPedidosEviados(String emailCliente) { // si quieres corrige a getPedidosEnviados
        try {
            return pedidoDAO.obtenerEnviados(emailCliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error leyendo pedidos enviados", e);
        }
    }
}