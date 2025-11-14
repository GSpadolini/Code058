package com.code058.controller;

import com.code058.exceptions.DuplicadosException;
import com.code058.model.*;
import com.code058.view.VistaConsola;

import java.time.LocalDateTime;
import java.util.List;

public class Controlador {
    // El Controlador tiene referencias a ambas capas
    private GestorDeDatos modelo;
    private VistaConsola vista;

    // Constructor que recibe las instancias
    public Controlador(GestorDeDatos modelo, VistaConsola vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    // El metodo de arranque que pide la App.java
    public void iniciar(){

            int opcion;
        try {
            do {
                vista.mostrarMenuPrincipal();
                opcion = vista.pedirOpcion();

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
                        vista.mostrarError("Opción no válida. introduce un número del menú(1 2 3 o 0).");
                        break;
                }
            } while (opcion != 0);
        } catch (Exception e) {
            vista.mostrarError("ERROR CRÍTICO DEL SISTEMA: " + e.getMessage());
            System.exit(1);
        }
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
        }catch (Exception e){
            vista.mostrarError("ERROR al añadir artículo en la BBDD: " + e.getMessage());
        }

    }

    private void mostrarArticulos(){
        try {
            // CORREGIDO: Ahora devuelve List<Articulo> y debe manejar la excepción.
            java.util.List<Articulo> articulos = modelo.getArticulos();
            vista.imprimirListaArticulos(articulos); // Asume que la vista acepta List
        } catch (Exception e) {
            vista.mostrarError("ERROR al listar artículos: " + e.getMessage());
        }
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
        }catch (Exception e){
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


        try {
            // Llama al Modelo, que delega al ClienteDAO.insertar()
            modelo.anadirCliente(nuevo);
            vista.mostrarMensaje("Cliente " + nuevo.getEmail() + " añadido con éxito.");
        } catch (Exception e) { // <-- CORREGIDO: Capturar la excepción genérica
            // Esto captura tanto los errores de SQL (duplicados de PK) como errores de conexión.
            vista.mostrarError("ERROR al añadir cliente en la BBDD. Causa: " + e.getMessage());
        }
    }

    private void mostrarClientes(){
        try {
            java.util.List<Cliente> clientes = modelo.getClientes();
            vista.imprimirListaClientes(clientes);
        } catch (Exception e) {
            vista.mostrarError("ERROR al listar todos los clientes desde la BBDD: " + e.getMessage());
        }
    }

    private void mostrarClientesEstandar(){
        try {
            vista.imprimirListaClientesFiltrados(modelo.getClientesEstandar());
        } catch (Exception e) {
            vista.mostrarError("ERROR al listar clientes Estándar desde la BBDD: " + e.getMessage());
        }
    }

    private void mostrarClientesPremium(){
        try {
            vista.imprimirListaClientesFiltrados(modelo.getClientesPremium());
        } catch (Exception e) {
            vista.mostrarError("ERROR al listar clientes Premium desde la BBDD: " + e.getMessage());
        }
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
                    eliminarPedido();
                    break;
                case 3:
                    mostrarPedidosPendientes();
                    break;
                case 4:
                    mostrarPedidosPendienteConFiltradoDeCliente();
                    break;
                case 5:
                    mostrarPedidosEnviados();
                    break;
                case 6:
                    mostrarPedidosEnviadosConFiltradoDeCliente();
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
        Cliente clienteParaPedido = null;

        try {
            clienteParaPedido = modelo.getCliente(email);

            if (clienteParaPedido != null){
                vista.mostrarMensaje("Cliente encontrado: " + clienteParaPedido.getEmail());
            } else {
                vista.mostrarError("El cliente con email " + email + " no existe.");
                vista.mostrarMensaje("¿Desea crear un nuevo cliente con este email? (S/N)");
                String respuesta = vista.pedirString();

                if(respuesta.equalsIgnoreCase("S")){
                    anadirCliente(email);
                    clienteParaPedido = modelo.getCliente(email);
                } else {
                    vista.mostrarMensaje("Pedido no creado. Volviendo al menú de pedidos.");
                    return;
                }
            }
            vista.mostrarMensaje("Ahora, añade el codigo del artículo al pedido(El articulo debe existir).");
            String codigoArticulo = vista.pedirString();
            Articulo articuloParaPedido = modelo.getArticulo(codigoArticulo);

            if (articuloParaPedido != null) {
                vista.mostrarMensaje("Artículo encontrado: " + articuloParaPedido.getCodigo());
            } else {
                vista.mostrarError("El artículo con código " + codigoArticulo + " no existe. No se puede crear el pedido.");
                return;
            }

            int numeroPedido = 0;// Para que la BBDD lo ignore y lo genere automaticamente
            vista.mostrarMensaje("Cantidad:");
            int cantidad = vista.pedirInt();
            LocalDateTime fechaPedido = LocalDateTime.now();
            double gastoEnvio = articuloParaPedido.getGastoEnvio() * (1-clienteParaPedido.descuentoEnvio());
            int tiempoPreparacion = articuloParaPedido.getTiempoPreparacionMin();

            nuevoPedido = new Pedido(clienteParaPedido, articuloParaPedido, numeroPedido, cantidad, fechaPedido, gastoEnvio, tiempoPreparacion);

            modelo.crearPedido(nuevoPedido);
            vista.mostrarMensaje("Pedido creado con exito.");

        } catch (Exception e) { // <<-- CAPTURAR CUALQUIER ERROR DE BBDD/DAO
            vista.mostrarError("ERROR al procesar el pedido o en la BBDD: " + e.getMessage());
        }
    }

    private void eliminarPedido(){
        vista.mostrarMensaje("Introduce el numero de pedido a eliminar:");
        int numPedido = vista.pedirInt();

        try{
            String mensaje = modelo.eliminarPedido(numPedido);
            vista.mostrarMensaje(mensaje);
        } catch (com.code058.exceptions.PedidoNoCancelableException e){
            vista.mostrarError(e.getMessage());
        } catch (Exception e){
            vista.mostrarError("ERROR al intentar eliminar el pedido: " + e.getMessage());
        }
    }

    private void mostrarPedidosPendientes(){
        try {
            vista.mostrarPedidos(modelo.getPedidosPendientes());
        } catch (Exception e) {
            vista.mostrarError("ERROR al listar pedidos pendientes: " + e.getMessage());
        }
    }

    private void mostrarPedidosPendienteConFiltradoDeCliente(){
        vista.mostrarMensaje("Ingresa un email de cliente");
        String email = vista.pedirString();
        try {
            List<Pedido> pedidos = modelo.getPedidosPendientes(email);

            if (pedidos.isEmpty()){
                vista.mostrarMensaje("El cliente con el email:" + email + " no tiene pedidos pendientes");
            }else {
                vista.mostrarPedidos(pedidos);
            }
        } catch (Exception e) {
            vista.mostrarError("ERROR al listar pedidos pendientes por cliente: " + e.getMessage());
        }
    }

    private void mostrarPedidosEnviados(){
        try {
            vista.mostrarPedidos(modelo.getPedidosEviados());
        } catch (Exception e) {
            vista.mostrarError("ERROR al listar pedidos enviados: " + e.getMessage());
        }
    }

    private void mostrarPedidosEnviadosConFiltradoDeCliente(){
        vista.mostrarMensaje("Ingresa un email de cliente");
        String email = vista.pedirString();
        try {
            List<Pedido> pedidos = modelo.getPedidosEviados(email);

            if (pedidos.isEmpty()){
                vista.mostrarMensaje("El cliente con el email:" + email + " no tiene pedidos enviados");
            }else {
                vista.mostrarPedidos(pedidos);
            }
        } catch (Exception e) {
            vista.mostrarError("ERROR al listar pedidos enviados por cliente: " + e.getMessage());
        }

    }


}


