package com.code058.view;

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
        } catch (java.util.InputMismatchException e) {

            scanner.next();
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
        palabra = scanner.next();
        return palabra;
    }

    public double pedirDouble(){
        double numeroDecimal;
        numeroDecimal = scanner.nextDouble();
        return numeroDecimal;
    }

    public int pedirInt(){
        int numeroNoDecimal;
        numeroNoDecimal = scanner.nextInt();
        return numeroNoDecimal;
    }

    public void mostrarMenuArticulos(){
        System.out.println("Elige una opcion");
        System.out.printf("1. Añadir Articulo");
        System.out.printf("\n2. Mostrat Articulo");
    }

}
