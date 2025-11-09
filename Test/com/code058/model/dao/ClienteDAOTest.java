package com.code058.model.dao;

import com.code058.model.Cliente;
import com.code058.model.ClienteEstandar;
import com.code058.model.ClientePremium;
import com.code058.model.dao.ClienteDAO;
import com.code058.model.dao.mysql.MySQLClienteDAO;

import java.util.List;

public class ClienteDAOTest {

    public static void main(String[] args) {
        ClienteDAO clienteDAO = new MySQLClienteDAO();

        // 1. Clientes de Prueba
        // Cliente Premium (debe guardar 30.00 en cuota_anual)
        Cliente premium = new ClientePremium("pablo@test.com", "Pablo Ruiz", "Calle 10", "44444444P");

        // Cliente Estándar (debe guardar 0.0 en cuota_anual)
        Cliente estandar = new ClienteEstandar("luisa@test.com", "Luisa Sanz", "Avenida 20", "55555555L");

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
                System.out.println("Email: " + c.getEmail()  + " | Instancia Java: " + tipoClase);
            }

        } catch (Exception e) {
            System.err.println("\n*** FALLO CRÍTICO EN LA PRUEBA ***");
            e.printStackTrace();
        }
    }
}
