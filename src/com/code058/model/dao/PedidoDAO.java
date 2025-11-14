package com.code058.model.dao;

import com.code058.model.Pedido;
import java.util.List;

public interface PedidoDAO {

    public Pedido obtenerPorNumero(int numeroPedido) throws Exception;
    // La operación clave: añadir un pedido (que debe ser transaccional)
    public void insertar(Pedido pedido) throws Exception;

    // Métodos para listar (que ya veremos cómo implementar)
    public List<Pedido> obtenerPendientes(String emailCliente) throws Exception;
    public List<Pedido> obtenerEnviados(String emailCliente) throws Exception;

    // Método para eliminar
    public void eliminar(int numeroPedido) throws Exception;
}
