package com.code058.model;

import com.code058.exceptions.PedidoNoCancelableException;
import org.junit.Test;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class GestorDeDatosPedidoTest {

    @Test(expected = PedidoNoCancelableException.class) // <<-- Este test espera la excepción de negocio
    public void testEliminarPedidoCancelableExcepcion() throws Exception { // <<-- throws Exception
        GestorDeDatos gestor = new GestorDeDatos();
        Cliente cliente = new ClienteEstandar("Laura_DAO", "Calle dos 2", "321", "laura_dao@email.com"); // Nuevos datos
        Articulo articulo = new Articulo("A2_DAO", "Reloj", 50.0, 5.0, 60);

        // 1. Insertar a través del método de negocio (DAO)
        // Sustituye: gestor.getClientes().put(cliente.getEmail(), cliente);
        // Sustituye: gestor.getArticulos().put(articulo.getCodigo(), articulo);
        gestor.anadirCliente(cliente);
        gestor.anadirArticulo(articulo);

        // Pedido con tiempo de preparación vencido (Necesita la lógica POO de fecha)
        // Para simular "no cancelable", necesitarías un pedido antiguo, pero para el test de excepción
        // usamos un pedido que DEBERÍA lanzar la excepción de negocio.
        Pedido pedido = new Pedido(cliente, articulo, 1, 1, LocalDateTime.now().minusMinutes(100), // Simular pedido antiguo
                articulo.getGastoEnvio(), articulo.getTiempoPreparacionMin());

        gestor.crearPedido(pedido);

        // Intentamos eliminarlo, debería lanzar PedidoNoCancelableException
        gestor.eliminarPedido(1);
    }

    @Test
    public void testAanadirArticuloDuplicadoExcepcion() throws Exception { // <<-- throws Exception
        GestorDeDatos gestor = new GestorDeDatos();
        Cliente cliente = new ClienteEstandar("Luis_DAO", "Calle uno 1", "123", "luis_dao@email.com");
        Articulo artiulo= new Articulo("A1_DAO", "Zapato", 20.0, 3.0, 60);

        // 1. Insertar a través del método de negocio (DAO)
        gestor.anadirCliente(cliente);
        gestor.anadirArticulo(artiulo);

        // Pedido reciente, cancelable (Necesita el ID del pedido)
        // Nota: Asignar un ID de pedido real es complejo en test. Usaremos 1.
        Pedido pedido = new Pedido(cliente, artiulo, 1, 2, LocalDateTime.now(), artiulo.getGastoEnvio(), artiulo.getTiempoPreparacionMin());

        gestor.crearPedido(pedido); // Esto inserta y asigna el ID (ej: 1)

        // Asumimos que el DAO asignó el ID 1.
        int idPedidoAsignado = 1;

        // Eliminamos sin lanzar excepción
        gestor.eliminarPedido(idPedidoAsignado);

        // 2. Comprobamos que se haya eliminado (Usando el nuevo método de listado)
        // Sustituye: assertTrue(gestor.getPedidos().isEmpty());
        assertTrue(gestor.getPedidosPendientes().isEmpty()); // <<-- Corregido
    }
}
