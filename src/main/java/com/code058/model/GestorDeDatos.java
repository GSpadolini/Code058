package com.code058.model;

import com.code058.exceptions.DuplicadosException;
import com.code058.exceptions.PedidoNoCancelableException;
import com.code058.model.dao.*;
import com.code058.model.dao.jdbc.MySQLDAOFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class GestorDeDatos {

    private final ArticuloDAO articuloDAO;
    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;

    public GestorDeDatos() {
        // Inicializamos los DAO a través de la fábrica
        DAOFactory factory = new MySQLDAOFactory();
        this.articuloDAO = factory.getArticuloDAO();
        this.clienteDAO = factory.getClienteDAO();
        this.pedidoDAO = factory.getPedidoDAO();
    }


    // GESTIÓN DE ARTÍCULOS
    public void anadirArticulo(Articulo articulo) throws DuplicadosException {
        try {
            if (articuloDAO.existeCodigo(articulo.getCodigo())) {
                throw new DuplicadosException("Error: El artículo con código " + articulo.getCodigo() + " ya existe.");
            }
            articuloDAO.insertar(articulo);
            System.out.println("✅ Artículo añadido correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar artículo: " + e.getMessage());
        }
    }

    public Map<String, Articulo> getArticulos() {
        try {
            return articuloDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los artículos", e);
        }
    }

    // GESTIÓN DE CLIENTES
    public void anadirCliente(Cliente cliente) throws DuplicadosException {
        try {
            if (clienteDAO.existeEmail(cliente.getEmail())) {
                throw new DuplicadosException("Error: el cliente con email " + cliente.getEmail() + " ya existe.");
            }
            clienteDAO.insertar(cliente);
            System.out.println("Cliente añadido correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
        }
    }

    public Map<String, Cliente> getClientes() {
        try {
            return clienteDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los clientes", e);
        }
    }

    public List<Cliente> getClientesEstandar() {
        try {
            return getClientes().values().stream()
                    .filter(c -> c instanceof ClienteEstandar)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar clientes estándar", e);
        }
    }

    public List<Cliente> getClientesPremium() {
        try {
            return getClientes().values().stream()
                    .filter(c -> c instanceof ClientePremium)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar clientes premium", e);
        }
    }


    // GESTIÓN DE PEDIDOS
    public void crearPedido(Pedido pedido) {
        try {
            pedidoDAO.insertar(pedido);
            System.out.println("Pedido creado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
        }
    }

    public void eliminarPedido(int numPedido) throws PedidoNoCancelableException {
        try {
            // Si tu clase Pedido tiene lógica interna para cancelación, deberías consultar antes de borrar
            List<Pedido> pedidos = pedidoDAO.listarTodos();
            for (Pedido p : pedidos) {
                if (p.getNumeroPedido() == numPedido) {
                    if (!p.esCancelable()) {
                        throw new PedidoNoCancelableException("El pedido " + numPedido + " no puede cancelarse (ya enviado).");
                    }
                    pedidoDAO.eliminar(numPedido);
                    System.out.println("Pedido eliminado correctamente.");
                    return;
                }
            }
            System.out.println("Pedido no encontrado.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
        }
    }

    public List<Pedido> getPedidos() {
        try {
            return pedidoDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los pedidos", e);
        }
    }

    public List<Pedido> getPedidosPendientes(String emailCliente) {
        try {
            return pedidoDAO.listarTodos().stream()
                    .filter(p -> p.esCancelable() &&
                            (emailCliente == null || p.getClienteEmail().equalsIgnoreCase(emailCliente)))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener pedidos pendientes", e);
        }
    }

    public List<Pedido> getPedidosEnviados(String emailCliente) {
        try {
            return pedidoDAO.listarTodos().stream()
                    .filter(p -> !p.esCancelable() &&
                            (emailCliente == null || p.getClienteEmail().equalsIgnoreCase(emailCliente)))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener pedidos enviados", e);
        }
    }

}

