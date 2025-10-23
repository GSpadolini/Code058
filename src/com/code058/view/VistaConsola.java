package com.code058.view;

import com.code058.model.Articulo;
import com.code058.model.Cliente;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VistaConsola {
    private Scanner scanner;

    public VistaConsola() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenuPrincipal() {
        System.out.println("\n==================================");
        System.out.println("||        MENU PRINCIPAL        ||");
        System.out.println("==================================");
        System.out.println("1. Gestion de Articulos");
        System.out.println("2. Gestion de Clientes");
        System.out.println("3. Gestion de Pedidos");
        System.out.println("0. Salir de la aplicacion");
        System.out.println("==================================");
    }

    public int pedirOpcion() {
        int opcion = -1;
        System.out.println("Selecciona una opcion: ");

        try {
            opcion = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {

            scanner.nextLine();
            mostrarError("Entrada no válida. Por favor, introduce un número.");
            opcion = -1;
        }
        return opcion;
    }

    public void mostrarMensaje(String mensaje){
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("!!! ERROR: " + error);
    }

    public String pedirString(){
        String palabra;
        palabra = scanner.nextLine();
        return palabra;
    }

    public double pedirDouble(){
        double numeroDecimal = 0.0;
        boolean valido = false;

        do {
            String numeroString = scanner.nextLine();

            try {
                numeroDecimal = Double.parseDouble(numeroString);
                valido = true;
            } catch (NumberFormatException e) {
                mostrarError("Formato incorrecto. Por favor, introduce un número decimal");
            }
        } while (!valido);

        return numeroDecimal;
    }

    public int pedirInt(){
        int numeroNoDecimal = 0;
        boolean valido = false;
        do {
            String numeroString = scanner.nextLine();
            try {
                numeroNoDecimal = Integer.parseInt(numeroString);
                valido = true;
            } catch (NumberFormatException e) {
                mostrarError("Formato incorrecto. Por favor, introduce un número entero");
            }
        } while (!valido);
        return numeroNoDecimal;
    }

    public void mostrarMenuArticulos(){
        System.out.println("Menu Gestion de Articulo");
        System.out.println("Elige una opcion");
        System.out.println("1. Añadir Articulo");
        System.out.println("2. Mostrat Articulo");
        System.out.println("0. Ir al menu principal");
    }
    public void imprimirListaArticulos(Map<String, Articulo> articulos) {
        articulos.forEach((codigo, articulo) ->
                System.out.println("Codigo del Articulo: " + codigo + ", " + articulo)
        );
    }

    public void imprimirListaClientes(Map<String, Cliente> clientes){
        clientes.forEach((email,cliente) ->
                System.out.println("Email " + email + ", " + cliente));
    }

    public void imprimirListaClientesFiltrados(List<Cliente> lista){
        if(lista.isEmpty()){
            System.out.println("No hay clientes en esta categoría");
        }else{
            lista.forEach(c -> System.out.println(c.toString()));
        }
    }

    public void mostrarMenuCliente(){
        System.out.println("Menu Gestion de Cliente");
        System.out.println("Elige una opcion");
        System.out.println("1. Añadir Cliente");
        System.out.println("2. Mostrar Cliente Estandar");
        System.out.println("3. Mostrar Cliente Estandar");
        System.out.println("4. Mostrar Cliente Primium");
        System.out.println("0. Ir al menu principal");
    }

}
