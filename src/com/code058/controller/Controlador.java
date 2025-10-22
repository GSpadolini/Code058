package com.code058.controller;

import com.code058.model.Articulo;
import com.code058.model.GestorDeDatos;
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
                        gestionarClientes(); // Por hacer
                        break;
                    case 3:
                        // gestionarPedidos(); // Por hacer
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

        modelo.anadirArticulo(nuevoArticulo);

        vista.mostrarMensaje("Artículo " + codigo + " añadido con éxito.");
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

    private void gestionarClientes() {

        int opcion;
        do {
            vista.mostrarMenuCliente();
            opcion = vista.pedirOpcion();
            switch (opcion) {
                case 1:
                    // anadirCliente(); // Por hacer
                    break;
                case 2:
                    // mostrarClientes(); // Por hacer
                    break;

                case 3:
                    // mostrarClientesEstandar(); // Por hacer
                    break;

                case 4:
                    // mostrarClientesPremium(); // Por hacer
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
    

}


