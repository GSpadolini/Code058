package com.code058.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorDeDatos {

    // Uso de Genéricos: Map<Clave, Valor> y List<Elemento>
    private Map<String, Cliente> clientes;
    private Map<String, Articulo> articulos;
    private List<Pedido> pedidos;

    public GestorDeDatos() {
        // Inicialización de las estructuras de datos dinámicas
        this.clientes = new HashMap<>(); // Diccionario
        this.articulos = new HashMap<>(); // Diccionario
        this.pedidos = new ArrayList<>(); // Lista

        // Opcional: Cargar datos de prueba para empezar a probar la aplicación
        cargarDatosIniciales();
    }

    // Aquí irán los métodos de lógica de negocio (ej: anadirCliente(), crearPedido(), etc.)

    private void cargarDatosIniciales() {
        // Ejemplo de carga para pruebas:
        ClientePremium cp = new ClientePremium("G. Spadolini", "Calle padilla 123", "X1234567Z", "giancarlo@uoc.edu");
        // Usamos el email como clave para el HashMap
        this.clientes.put(cp.getEmail(), cp);

        Articulo a1 = new Articulo("REF001", "Laptop de 15 pulg.", 800.0, 10.0, 60);
        this.articulos.put(a1.getCodigo(), a1);
    }

}
