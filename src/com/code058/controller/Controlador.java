package com.code058.controller;

import com.code058.exceptions.DuplicadosException;
import com.code058.model.*;
import com.code058.view.VistaConsola;

import java.time.LocalDateTime;
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
    public void iniciar() throws DuplicadosException {

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
                        gestionarPedidos(); // Por hacer
                        break;
                    case 0:
                        vista.mostrarMensaje("Saliendo de la aplicación. ¡Hasta pronto!");
                        break;
                    default:
                        vista.mostrarError("Opción no válida. introduce un número del menú(1 2 3 o 0).");
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
    private void gestionarClientes(){

        int opcion;
        do {
            vista.mostrarMenuCliente();
            opcion = vista.pedirOpcion();
            switch (opcion) {
                case 1:
                    anadirCliente();
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
    private void anadirCliente(){
        vista.mostrarMensaje("Nombre: ");
        String nombre = vista.pedirString();
        vista.mostrarMensaje("Domicilio: ");
        String domicilio = vista.pedirString();
        vista.mostrarMensaje("NIF: ");
        String nif = vista.pedirString();
        vista.mostrarMensaje("Email:");
        String email = vista.pedirString();
        vista.mostrarMensaje("¿Tipo de cliente? (1 = Estandar, 2 = Premium)");
        int tipo = vista.pedirInt();

        Cliente nuevo = null;

        do {
            if (tipo != 1 && tipo != 2) {
                vista.mostrarMensaje("Tipo no válido. Introduzca 1 para Estandar o 2 para Premium:");
                tipo = vista.pedirInt();
            }
            if(tipo == 2){
                nuevo = new ClientePremium(nombre, domicilio, nif, email);
            }
            if (tipo == 1){
                nuevo = new ClienteEstandar(nombre, domicilio, nif, email);
            }
        } while (tipo != 1 && tipo != 2);


        try{
            modelo.anadirCliente(nuevo);
            vista.mostrarMensaje("Cliente " + email + " añadido con éxito.");
        }catch (com.code058.exceptions.DuplicadosException e){
            vista.mostrarError(e.getMessage()); // La maneja y usa la VISTA para el error
        }
    }

    private void anadirCliente(String email){
        vista.mostrarMensaje("Nombre: ");
        String nombre = vista.pedirString();
        vista.mostrarMensaje("Domicilio: ");
        String domicilio = vista.pedirString();
        vista.mostrarMensaje("NIF: ");
        String nif = vista.pedirString();
        vista.mostrarMensaje("¿Tipo de cliente? (1 = Estandar, 2 = Premium)");
        int tipo = vista.pedirInt();

        Cliente nuevo = null;

        do {
            if (tipo != 1 && tipo != 2) {
                vista.mostrarMensaje("Tipo no válido. Introduzca 1 para Estandar o 2 para Premium:");
                tipo = vista.pedirInt();
            }
            if(tipo == 2){
                nuevo = new ClientePremium(nombre, domicilio, nif, email);
            }
            if (tipo == 1){
                nuevo = new ClienteEstandar(nombre, domicilio, nif, email);
            }
        } while (tipo != 1 && tipo != 2);


        try{
            modelo.anadirCliente(nuevo);
            vista.mostrarMensaje("Cliente " + email + " añadido con éxito.");
        }catch (com.code058.exceptions.DuplicadosException e){
            vista.mostrarError(e.getMessage()); // La maneja y usa la VISTA para el error
        }
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

    private void gestionarPedidos() {

        int opcion;
        do {
            vista.mostrarMenuPedidos();
            opcion = vista.pedirOpcion();
            switch (opcion) {
                case 1:
                    anadirPedido();
                    break;
                case 2:
                    //eliminarPedido();
                    break;
                case 3:
                    //mostrarPedidosPendientes();
                    break;
                case 4:
                    //mostrarPedidosPendienteConFiltradoDeCliente();
                    break;
                case 5:
                    //mostrarPedidosEnviados();
                    break;
                case 6:
                    //mostrarPedidosEnviadosConFiltradoDeCliente();
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

    private void anadirPedido(){
        Pedido nuevoPedido = null;
        vista.mostrarMensaje("Introduce el email del cliente:");

        String email = vista.pedirString();
        boolean clienteExiste = modelo.getClientes().containsKey(email);
        Cliente clienteParaPedido = null;

        if(clienteExiste){
            clienteParaPedido = modelo.getClientes().get(email);
            vista.mostrarMensaje("Cliente encontrado: " + clienteParaPedido.getEmail());
            // Continuar con la creación del pedido...
        } else {
            vista.mostrarError("El cliente con email " + email + " no existe. No se puede crear el pedido.");
            vista.mostrarMensaje("¿Desea crear un nuevo cliente con este email? (S/N)");
            String respuesta = vista.pedirString();
            if(respuesta.equalsIgnoreCase("S")){

                anadirCliente(email);
                Cliente clienteRecuperado = modelo.getClientes().get(email);
                clienteParaPedido = clienteRecuperado;

            } else {
                vista.mostrarMensaje("Pedido no creado. Volviendo al menú de pedidos.");
                return;
            }
        }

        Articulo articuloParaPedido = null;
        int numeroPedidoTemporal = 0;

        vista.mostrarMensaje("Ahora, añade el codigo del artículo al pedido(El articulo debe existir).");
        String codigoArticulo = vista.pedirString();
        boolean articuloExiste = modelo.getArticulos().containsKey(codigoArticulo);
        if (articuloExiste) {
                articuloParaPedido = modelo.getArticulos().get(codigoArticulo);
                vista.mostrarMensaje("Artículo encontrado: " + articuloParaPedido.getCodigo());
        } else {
                vista.mostrarError("El artículo con código " + codigoArticulo + " no existe. No se puede crear el pedido.");
                return;
        }
        int numeroPedido = numeroPedidoTemporal;//esto porque se genera en el modelo(GestorDeDatos) en el metodo gerarNumeroPedido()
        vista.mostrarMensaje("Cantidad:");
        int cantidad = vista.pedirInt();
        LocalDateTime fechaPedido = LocalDateTime.now();
        double gastoEnvio = articuloParaPedido.getGastoEnvio() * (1-clienteParaPedido.descuentoEnvio());
        int tiempoPreparacion = articuloParaPedido.getTiempoPreparacionMin();

        nuevoPedido = new Pedido(clienteParaPedido, articuloParaPedido, numeroPedido, cantidad, fechaPedido, gastoEnvio, tiempoPreparacion);

        modelo.crearPedido(nuevoPedido);
        vista.mostrarMensaje("Pedido creado con exito.");


    }


}


