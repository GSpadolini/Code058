package com.code058.controller;

import com.code058.exceptions.DuplicadosException;
import com.code058.model.*;
import com.code058.view.VistaConsola;

import java.time.LocalDateTime;
import java.util.List;
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

    private void mostrarArticulos() {
        List<Articulo> articulos = modelo.getArticulos();
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

    private void mostrarClientes() {
        List<Cliente> clientes = modelo.getClientes();
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
                    eliminarPedido();
                    break;
                case 3:
                    mostrarPedidosPendientes();
                    break;
                case 4:
                    mostrarPedidosPendienteConFiltradoDeCliente();
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

    private void anadirPedido() {
        Pedido nuevoPedido = null;
        vista.mostrarMensaje("=== Crear nuevo pedido ===");
        vista.mostrarMensaje("Introduce el email del cliente:");

        String email = vista.pedirString();

        // 1) Buscar cliente en la lista (modelo.getClientes() devuelve List<Cliente>)
        Cliente clienteParaPedido = null;
        for (Cliente c : modelo.getClientes()) {
            if (c.getEmail() != null && c.getEmail().equalsIgnoreCase(email)) {
                clienteParaPedido = c;
                break;
            }
        }

        // 2) Si no existe, preguntar si crear uno nuevo y recoger datos
        if (clienteParaPedido == null) {
            vista.mostrarError("El cliente con email " + email + " no existe.");
            vista.mostrarMensaje("¿Desea crear un nuevo cliente con este email? (S/N)");
            String respuesta = vista.pedirString();
            if (respuesta.equalsIgnoreCase("S")) {
                // Pedimos los datos necesarios para crear el cliente
                vista.mostrarMensaje("Introduce el nombre:");
                String nombre = vista.pedirString();
                vista.mostrarMensaje("Introduce el domicilio:");
                String domicilio = vista.pedirString();
                vista.mostrarMensaje("Introduce el NIF:");
                String nif = vista.pedirString();

                // Elegir tipo: estandar o premium
                vista.mostrarMensaje("Tipo de cliente: 1=Estandar, 2=Premium (introduce 1 o 2):");
                int tipo = vista.pedirInt();
                if (tipo == 2) {
                    // Cliente Premium
                    ClientePremium cp = new ClientePremium();
                    cp.setEmail(email);
                    cp.setNombre(nombre);
                    cp.setDomicilio(domicilio);
                    cp.setNif(nif);
                    // si quieres un valor distinto de cuota, pídelo; por defecto tu constructor pone 30.00
                    try {
                        modelo.anadirCliente(cp);
                        clienteParaPedido = cp;
                        vista.mostrarMensaje("Cliente premium creado y añadido.");
                    } catch (DuplicadosException e) {
                        vista.mostrarError("No se pudo crear el cliente: " + e.getMessage());
                        return;
                    }
                } else {
                    // Cliente Estandar
                    ClienteEstandar ce = new ClienteEstandar();
                    ce.setEmail(email);
                    ce.setNombre(nombre);
                    ce.setDomicilio(domicilio);
                    ce.setNif(nif);
                    try {
                        modelo.anadirCliente(ce);
                        clienteParaPedido = ce;
                        vista.mostrarMensaje("Cliente estandar creado y añadido.");
                    } catch (DuplicadosException e) {
                        vista.mostrarError("No se pudo crear el cliente: " + e.getMessage());
                        return;
                    }
                }
            } else {
                vista.mostrarMensaje("Pedido no creado. Volviendo al menú de pedidos.");
                return;
            }
        } else {
            vista.mostrarMensaje("Cliente encontrado: " + clienteParaPedido.getEmail());
        }

        // 3) Buscar artículo por código en la lista
        vista.mostrarMensaje("Ahora, añade el codigo del artículo al pedido (El articulo debe existir).");
        String codigoArticulo = vista.pedirString();
        Articulo articuloParaPedido = null;
        for (Articulo a : modelo.getArticulos()) {
            if (a.getCodigo() != null && a.getCodigo().equalsIgnoreCase(codigoArticulo)) {
                articuloParaPedido = a;
                break;
            }
        }
        if (articuloParaPedido == null) {
            vista.mostrarError("El artículo con código " + codigoArticulo + " no existe. No se puede crear el pedido.");
            return;
        } else {
            vista.mostrarMensaje("Artículo encontrado: " + articuloParaPedido.getCodigo());
        }

        // 4) Recoger cantidad y crear pedido
        vista.mostrarMensaje("Cantidad:");
        int cantidad = vista.pedirInt();
        LocalDateTime fechaPedido = LocalDateTime.now();
        double gastoEnvio = articuloParaPedido.getGastoEnvio() * (1 - clienteParaPedido.descuentoEnvio());
        int tiempoPreparacion = articuloParaPedido.getTiempoPreparacionMin();

        // Número temporal (0). En la versión con BD el DAO/BD asignará el número real (AUTO_INCREMENT)
        int numeroPedidoTemporal = 0;

        nuevoPedido = new Pedido(clienteParaPedido, articuloParaPedido, numeroPedidoTemporal,
                cantidad, fechaPedido, gastoEnvio, tiempoPreparacion);

        // 5) Guardar pedido en el modelo (que delega a DAO/BD)
        modelo.crearPedido(nuevoPedido);

        // Si DAO asigna el número de pedido al objeto, lo mostramos:
        vista.mostrarMensaje("Pedido creado con exito. Número: " + nuevoPedido.getNumeroPedido());
    }


    private void eliminarPedido() {
        vista.mostrarMensaje("=== Eliminar Pedido ===");

        // Obtener todos los pedidos actuales
        java.util.List<com.code058.model.Pedido> listaPedidos = modelo.getPedidos();

        if (listaPedidos.isEmpty()) {
            vista.mostrarMensaje("No hay pedidos registrados.");
            return;
        }

        // Mostrar lista de pedidos
        vista.mostrarMensaje("Lista de pedidos actuales:");
        for (com.code058.model.Pedido p : listaPedidos) {
            vista.mostrarMensaje("Número: " + p.getNumeroPedido() +
                    " | Cliente: " + p.getCliente().getEmail() +
                    " | Artículo: " + p.getArticulo().getCodigo() +
                    " | Fecha: " + p.getFechaPedido() +
                    " | Tiempo preparación: " + p.getTiempoPreparacion() + " min");
        }

        // Pedir número del pedido a eliminar
        vista.mostrarMensaje("Introduce el número del pedido que deseas eliminar:");
        int numero = vista.pedirInt();

        try {
            String resultado = modelo.eliminarPedido(numero);

            if ("PEDIDO_ELIMINADO".equals(resultado)) {
                vista.mostrarMensaje("✅ Pedido " + numero + " eliminado correctamente.");
            } else if ("PEDIDO_NO_ENCONTRADO".equals(resultado)) {
                vista.mostrarMensaje("⚠️ No se encontró ningún pedido con el número " + numero + ".");
            } else {
                vista.mostrarMensaje("Resultado inesperado: " + resultado);
            }
        } catch (com.code058.exceptions.PedidoNoCancelableException e) {
            vista.mostrarError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarError("Error al eliminar el pedido: " + e.getMessage());
        }
    }


    private void mostrarPedidosPendientes() {
        vista.mostrarMensaje("=== Todos los Pedidos Pendientes ===");

        // Llamamos al modelo pasando null para obtener todos los pedidos pendientes
        List<Pedido> pedidosPendientes = modelo.getPedidosPendientes(null);

        // Mostramos la lista usando la vista
        vista.imprimirListaPedidos(pedidosPendientes);
    }


    private void mostrarPedidosPendienteConFiltradoDeCliente() {
        vista.mostrarMensaje("=== Pedidos Pendientes Filtrados por Cliente ===");

        // Pedimos el email del cliente
        vista.mostrarMensaje("Introduce el email del cliente:");
        String email = vista.pedirString();

        // Llamamos al modelo pasando el email
        List<Pedido> pedidosPendientes = modelo.getPedidosPendientes(email);

        // Mostramos la lista
        vista.imprimirListaPedidos(pedidosPendientes);
    }



}




