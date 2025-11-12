package com.code058.controller;

import com.code058.exceptions.DuplicadosException;
import com.code058.model.*;
import com.code058.view.VistaConsola;

import java.util.Map;

public class Controlador {
    // El Controlador tiene referencias a ambas capas
    private GestorDeDatos modelo;
    private VistaConsola vista;

    // Constructor que recibe las instancias
    public Controlador(GestorDeDatos modelo, VistaConsola vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    // El método de arranque que pide la App.java
    public void iniciar() {

            int opcion;
            do {
                vista.mostrarMenuPrincipal();
                // 1. Pide a la VISTA que muestre el menú y devuelva la opción
                opcion = vista.pedirOpcion();

                // 2. Ejecuta la lógica según la opción
                switch (opcion) {
                    case 1:
                        gestionarArticulos();
                        break;
                    case 2:
                        gestionarClientes();
                        break;
                    case 3:
                        gestionarPedidos();
                        break;
                    case 0:
                        vista.mostrarMensaje("Saliendo de la aplicación. ¡Hasta pronto!");
                        break;
                    default:
                        // Si la opción devuelta por la Vista no es válida, no hacemos nada, se repite el bucle.
                        break;
                }
            } while (opcion != 0);
    }

    // Artículos
    private void anadirArticulo() {

        vista.mostrarMensaje("--- Añadir Nuevo Artículo ---");
        vista.mostrarMensaje("Introduce el codigo alfanumerico");
        String codigo = vista.pedirString();
        vista.mostrarMensaje("Introduce la descripcion");
        String descripcion = vista.pedirString();
        vista.mostrarMensaje("Introduce el precio");
        double precio = vista.pedirDouble();
        vista.mostrarMensaje("Introduce el gasto de envio");
        double gastoEnvio = vista.pedirDouble();
        vista.mostrarMensaje("Introduce el timepo de preparacion");
        int tiempoPreparacion = vista.pedirInt();

        Articulo nuevoArticulo = new Articulo(codigo, descripcion,precio,gastoEnvio,tiempoPreparacion);

        try{
            modelo.anadirArticulo(nuevoArticulo);
            vista.mostrarMensaje("Artículo " + codigo + " añadido con éxito.");
        }catch (com.code058.exceptions.DuplicadosException e){
            vista.mostrarError(e.getMessage());
        }

    }

    private void mostrarArticulos(){
        Map<String, Articulo> articulos = modelo.getArticulos();
        vista.imprimirListaArticulos(articulos);
    }

    private void gestionarArticulos() {

        int opcion;
        do {
            vista.mostrarMenuArticulos();
            opcion = vista.pedirOpcion();

            switch (opcion) {
                case 1:
                    anadirArticulo();
                    break;
                case 2:
                    mostrarArticulos();
                    break;
                case 0:
                    vista.mostrarMensaje("Volviendo al Menú Principal");
                    break;
                default:
                    vista.mostrarError("Opción no válida.");
                    break;
            }
        } while (opcion != 0);
    }

    //Pedidos
    private void gestionarClientes() {

        int opcion;
        do {
            vista.mostrarMenuCliente();
            opcion = vista.pedirOpcion();
            switch (opcion) {
                case 1:
                    try {
                        anadirCliente();
                    } catch (DuplicadosException e) {
                        vista.mostrarError(e.getMessage());
                    }
                    break;
                case 2:
                    mostrarClientes();
                    break;

                case 3:
                    mostrarClientesEstandar();
                    break;

                case 4:
                    mostrarClientesPremium();
                    break;
                case 0:
                    vista.mostrarMensaje("Volviendo al Menú Principal");
                    break;
                default:
                    vista.mostrarError("Opción no válida.");
                    break;
            }
        } while (opcion != 0);
    }

    //Clientes
    private void anadirCliente()throws DuplicadosException{
        vista.mostrarMensaje("Nombre: ");
        String nombre = vista.pedirString();
        vista.mostrarMensaje("Domicilio: ");
        String domicilio = vista.pedirString();
        vista.mostrarMensaje("NIF: ");
        String nif = vista.pedirString();
        vista.mostrarMensaje("Emal:");
        String email = vista.pedirString();
        vista.mostrarMensaje("¿Tipo de cliente? (1 = Estandar, 2  Premium)");
        int tipo = vista.pedirInt();

        Cliente nuevo;

        if(tipo ==2){
            nuevo = new ClientePremium(nombre, domicilio, nif, email);
        }else{
            nuevo = new ClienteEstandar(nombre, domicilio, nif, email);
        }

        modelo.anadirCliente(nuevo);
        vista.mostrarMensaje("Cliente añadido correctamente");
    }

    private void mostrarClientes(){
        Map<String, Cliente> clientes = modelo.getClientes();
        vista.imprimirListaClientes(clientes);
    }

    private void mostrarClientesEstandar(){
        vista.imprimirListaClientesFiltrados(modelo.getClientesEstandar());
    }

    private void mostrarClientesPremium(){
        vista.imprimirListaClientesFiltrados(modelo.getClientesPremium());
    }

    private void gestionarPedidos(){
        int opcion;
        do{
            vista.mostrarMensaje("-- GESTION DE PEDIDOS --");
            vista.mostrarMensaje("1. Crear Pedido");
            vista.mostrarMensaje("2. Mostrar Pedidos");
            vista.mostrarMensaje("0. Volver al Menu Principal");
            opcion = vista.pedirOpcion();

            switch (opcion){
                case 1:
                    crearPedido();
                    break;
                case 2:
                    mostrarPedido();
                    break;
                case 0:
                    vista.mostrarMensaje("Volviendo al Menú Princial");
                    break;
                default:
                    vista.mostrarMensaje("Opcion no valida.");
                    break;
            }
        } while (opcion !=0);
    }

    private void crearPedido() {
        vista.mostrarMensaje("Email del cliente: ");
        String email = vista.pedirString();

        vista.mostrarMensaje("Código del artículo: ");
        String codigo = vista.pedirString();

        vista.mostrarMensaje("Unidades: ");
        int unidades = vista.pedirInt();

        vista.mostrarMensaje("Gasto de envío: ");
        double gastoEnvio = vista.pedirDouble();

        vista.mostrarMensaje("Tiempo de preparación (min): ");
        int tiempoPrep = vista.pedirInt();

        Pedido nuevo = new Pedido(
                0,  // número de pedido (autoincremental)
                email,
                codigo,
                unidades,
                java.time.LocalDateTime.now(),
                gastoEnvio,
                tiempoPrep
        );

        modelo.crearPedido(nuevo);
    }

    private void mostrarPedido() {
        var pedidos = modelo.getPedidos();
        if (pedidos.isEmpty()) {
            vista.mostrarMensaje("No hay pedidos registrados.");
        } else {
            pedidos.forEach(p -> System.out.println(p.toString()));
        }
    }
}


