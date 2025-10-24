package com.code058.model;

import com.code058.exceptions.DuplicadosException;
import com.code058.exceptions.PedidoNoCancelableException;

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
        this.pedidos.add(pedido);
    }

    public void eliminarPedido(int numPedido) throws PedidoNoCancelableException{
        for(int i = 0; i < pedidos.size(); i++){
            Pedido p = pedidos.get(i);

            if( p.getNumeroPedido() == numPedido){
                if(!p.esCancelable()){
                    throw new PedidoNoCancelableException("El pedido " + numPedido + " no puede cancelarse (ya se ha enviado)");
                }

                pedidos.remove(i);
                System.out.println("Pedido eliminado correctamente");
                return;
            }
        }
        System.out.println("Pedido no encontrado");
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

    //Datos prueba
    private void cargarDatosIniciales() {
        // Ejemplo de carga para pruebas:
        ClientePremium cp = new ClientePremium("G. Spadolini", "Calle padilla 123", "X1234567Z", "giancarlo@uoc.edu");
        // Usamos el email como clave para el HashMap
        this.clientes.put(cp.getEmail(), cp);

        Articulo a1 = new Articulo("REF001", "Laptop de 15 pulg.", 800.0, 10.0, 60);
        this.articulos.put(a1.getCodigo(), a1);
    }

}
