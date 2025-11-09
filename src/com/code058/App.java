package com.code058;

import com.code058.controller.Controlador;
import com.code058.exceptions.DuplicadosException;
import com.code058.model.GestorDeDatos;
import com.code058.view.VistaConsola;
import com.code058.model.util.DBConnectionUtil;



import com.code058.model.Cliente;
import com.code058.model.ClienteEstandar;
import com.code058.model.ClientePremium;
import com.code058.model.dao.ClienteDAO;
import com.code058.model.dao.mysql.MySQLClienteDAO;

import java.util.List;


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








        ClienteDAO clienteDAO = new MySQLClienteDAO();

        // 1. Clientes de Prueba
        // Cliente Premium (debe guardar 30.00 en cuota_anual)
        Cliente premium = new ClientePremium("pablo@test.com", "Pablo Ruiz", "Calle 10", "44444444P");

        // Cliente Estándar (debe guardar 0.0 en cuota_anual)
        Cliente estandar = new ClienteEstandar("luisa@test.com", "Luisa Sanz", "Avenida 20", "55559555L");

        try {
            // --- Prueba de INSERCIÓN (CREATE) ---
            System.out.println("--- 1. Insertando Cliente Premium ---");
            clienteDAO.insertar(premium);
            System.out.println("Cliente Premium insertado: " + premium.getEmail());

            System.out.println("--- 2. Insertando Cliente Estándar ---");
            clienteDAO.insertar(estandar);
            System.out.println("Cliente Estándar insertado: " + estandar.getEmail());

            // --- Prueba de LECTURA (READ ALL) ---
            System.out.println("\n--- 3. Listando TODOS los clientes ---");
            List<Cliente> listaClientes = clienteDAO.obtenerTodos();

            for (Cliente c : listaClientes) {
                // Verificamos el tipo de objeto que se recuperó
                String tipoClase = (c instanceof ClientePremium) ? "PREMIUM" : "ESTANDAR";
                System.out.println("Email: " + c.getEmail() +  " | Instancia Java: "+ tipoClase);
            }

        } catch (Exception e) {
            System.err.println("\n*** FALLO CRÍTICO EN LA PRUEBA ***");
            e.printStackTrace();
        }
    }





}
