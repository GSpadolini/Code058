package com.code058.model;

import com.code058.exceptions.PedidoNoCancelableException;
import org.junit.Test;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class GestorDeDatosPedidoTest {

    @Test
    public void testEliminarPedidoCancelableExcepcion() throws PedidoNoCancelableException {
        GestorDeDatos gestor = new GestorDeDatos();
        Cliente cliente = new ClienteEstandar("Laura", "Calle dos 2", "321", "laura@email.com");
        Articulo articulo= new Articulo("A2", "Reloj", 50.0, 5.0, 1);

        //Añadimos cliente y artículo al gestor
        gestor.getClientes().put(cliente.getEmail(), cliente);
        gestor.getArticulos().put(articulo.getCodigo(), articulo);

        //Pedido con tiempo de preparación vencido - no cancelable
        Pedido pedido = new Pedido(cliente, articulo, 1,1, LocalDateTime.now().minusMinutes(10) ,articulo.getGastoEnvio(), articulo.getTiempoPreparacionMin());

        //Creamos pedido
        gestor.crearPedido(pedido);

        //Intentamos eliminarlo, lanza excepción
        gestor.eliminarPedido(1);
    }

    @Test
    public void testAanadirArticuloDuplicadoExcepcion() throws PedidoNoCancelableException {
        GestorDeDatos gestor = new GestorDeDatos();
        Cliente cliente = new ClienteEstandar("Luis", "Calle uno 1", "123", "luis@email.com");
        Articulo artiulo= new Articulo("A1", "Zapato", 20.0, 3.0, 60);

        //Añadimos cliente y artículo al gestor
        gestor.getClientes().put(cliente.getEmail(), cliente);
        gestor.getArticulos().put(artiulo.getCodigo(), artiulo);

        //Pedido reciente, cancelable
        Pedido pedido = new Pedido(cliente, artiulo, 1,2, LocalDateTime.now(), artiulo.getGastoEnvio(), artiulo.getTiempoPreparacionMin());

        //Creamos pedido
        gestor.crearPedido(pedido);

        //Eliminamos sin lanzar excepción
        gestor.eliminarPedido(1);

        //Comprobamos que se haya eliminado
        assertTrue(gestor.getPedidos().isEmpty());

    }
}
