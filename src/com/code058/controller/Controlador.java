package com.code058.controller;

import com.code058.model.GestorDeDatos;
import com.code058.view.VistaConsola;

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
        // Por ahora, solo un mensaje de prueba
        System.out.println("Sistema iniciado. ¡Bienvenido/a!");
        // Aquí irá el bucle do-while del menú (lo veremos en Módulo 2)
    }
}
