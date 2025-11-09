package com.code058;

import com.code058.controller.Controlador;
import com.code058.exceptions.DuplicadosException;
import com.code058.model.GestorDeDatos;
import com.code058.view.VistaConsola;
import com.code058.model.util.DBConnectionUtil;

public class App {
    public static void main(String[] args) throws DuplicadosException {
        System.out.println("--- Iniciando Test de Conexión ---");
        DBConnectionUtil.testConnection();
        System.out.println("----------------------------------");

        // --- PASO 1: Creación de instancias del Modelo ---
        // El Modelo es la lógica de negocio y donde viven los datos.
        // Vamos a asumir que tienes una clase central para gestionar todo.
        GestorDeDatos modelo = new GestorDeDatos();

        // --- PASO 2: Creación de la Vista ---
        // La Vista es responsable de interactuar con el usuario (mostrar menús, leer entradas).
        VistaConsola vista = new VistaConsola();

        // --- PASO 3: Creación del Controlador ---
        // El Controlador es el puente. Necesita saber del Modelo y la Vista.
        Controlador controlador = new Controlador(modelo, vista);

        // --- PASO 4: Iniciar la aplicación ---
        // El Controlador comienza el bucle de la aplicación (mostrar menú).
        controlador.iniciar();

    }

}
