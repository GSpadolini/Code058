package com.code058.model;

import com.code058.exceptions.DuplicadosException;
import com.code058.exceptions.PedidoNoCancelableException;

import java.time.LocalDateTime;
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

    //Gestión artículos
    public void anadirArticulo(Articulo articulo) throws DuplicadosException{
        if (this.articulos.containsKey(articulo.getCodigo())) {
            throw new DuplicadosException("Error de negocio: El artículo con código " + articulo.getCodigo() + " ya existe.");
        }
        this.articulos.put(articulo.getCodigo(), articulo);
    }

    public Map<String, Articulo> getArticulos() {
        return this.articulos;
    }


    //Gestión cliente
    public void anadirCliente(Cliente cliente) throws DuplicadosException{
        if(this.clientes.containsKey((cliente.getEmail()))){
            throw new DuplicadosException(("El cliente con el email " + cliente.getEmail()) + " ya existe");
        }
        this.clientes.put(cliente.getEmail(), cliente);
    }

    public Map<String, Cliente> getClientes(){ return  this.clientes; }

    public List<Cliente> getClientesEstandar(){
        List<Cliente> lista = new ArrayList<>();
        for(Cliente c : clientes.values()){
            if( c instanceof ClienteEstandar) lista.add(c);
        }
        return lista;
    }
    public List<Cliente> getClientesPremium(){
        List<Cliente> lista = new ArrayList<>();
        for(Cliente c : clientes.values()){
            if( c instanceof ClientePremium) lista.add(c);
        }
        return lista;
    }


    //Gestión pedidos
    public void crearPedido(Pedido pedido){
        pedido.setNumeroPedido(generarNumeroPedido());
        this.pedidos.add(pedido);
    }

    private int generarNumeroPedido(){
        if (pedidos.isEmpty()){
            return 1;
        }
        Pedido ultimoPedido = pedidos.get(pedidos.size() - 1);
        return ultimoPedido.getNumeroPedido() + 1;
    }

    public String eliminarPedido(int numPedido) throws PedidoNoCancelableException{
        for(int i = 0; i < pedidos.size(); i++){
            Pedido p = pedidos.get(i);

            if( p.getNumeroPedido() == numPedido){
                if(!p.esCancelable()){
                    throw new PedidoNoCancelableException("El pedido " + numPedido + " no puede cancelarse (ya se ha enviado)");
                }

                pedidos.remove(i);
                return "PEDIDO_ELIMINADO";
            }
        }
        return "PEDIDO_NO_ENCONTRADO";
    }

    public List<Pedido> getPedidos() {
        return this.pedidos;
    }

    public List<Pedido> getPedidosPendientes(String emailCliente){
        List<Pedido> resultado = new ArrayList<>();
        for(Pedido p : pedidos){
            boolean pendiente = p.esCancelable();
            boolean coincide = (emailCliente == null) || p.getCliente().getEmail().equalsIgnoreCase(emailCliente);
            if(pendiente && coincide){
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Pedido> getPedidosPendientes(){
        List<Pedido> resultado = new ArrayList<>();
        for(Pedido p : pedidos){
            boolean pendiente = p.esCancelable();
            if(pendiente){
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Pedido> getPedidosEviados(String emailCliente){
        List<Pedido> resultado = new ArrayList<>();
        for(Pedido p : pedidos){
            boolean pendiente = !p.esCancelable();
            boolean coincide = (emailCliente == null) || p.getCliente().getEmail().equalsIgnoreCase(emailCliente);
            if(pendiente && coincide){
                resultado.add(p);
            }
        }

        return resultado;
    }

    public List<Pedido> getPedidosEviados(){
        List<Pedido> resultado = new ArrayList<>();
        for(Pedido p : pedidos){
            boolean pendiente = !p.esCancelable();
            if (pendiente){
                resultado.add(p);
            }

        }
        return resultado;
    }


    //Datos prueba
    private void cargarDatosIniciales() {
        // Ejemplo de carga para pruebas:
        ClientePremium cp = new ClientePremium("asd", "Calle padilla 123", "X1234567Z", "asd@asd.com");
        // Usamos el email como clave para el HashMap
        this.clientes.put(cp.getEmail(), cp);

        ClienteEstandar cp1 = new ClienteEstandar("qwe", "Calle padilla 123", "X1324567Z", "qwe@qwe.com");
        this.clientes.put(cp1.getEmail(), cp1);

        Articulo a1 = new Articulo("asd1", "Laptop de 15 pulg.", 800.0, 10.0, 1);
        this.articulos.put(a1.getCodigo(), a1);

        Articulo a2 = new Articulo("asd2", "Laptop de 15 pulg.", 800.0, 10.0, 5);
        this.articulos.put(a2.getCodigo(), a2);

//        Pedido p1 = new Pedido(cp, a1, 1, 1, LocalDateTime.of(2025,10,26,13,23,0), 15.0, 5);
//        this.pedidos.add(p1);
//
//        Pedido p2 = new Pedido(cp1, a1 , 2, 3,LocalDateTime.now(), 20, 60);
//        this.pedidos.add(p2);
    }

}