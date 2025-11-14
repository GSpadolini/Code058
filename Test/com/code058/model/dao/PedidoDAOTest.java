package com.code058.model.dao;

import com.code058.model.Cliente;
import com.code058.model.Articulo;
import com.code058.model.ClienteEstandar;
import com.code058.model.Pedido;
import com.code058.model.dao.PedidoDAO;
import com.code058.model.dao.ClienteDAO; // Necesario para obtener el cliente
import com.code058.model.dao.ArticuloDAO; // Necesario para obtener el artículo
import com.code058.model.dao.mysql.MySQLPedidoDAO;
import com.code058.model.dao.mysql.MySQLClienteDAO;
import com.code058.model.dao.mysql.MySQLArticuloDAO;

import java.time.LocalDateTime;

public class PedidoDAOTest {
    public static void main(String[] args) {

        PedidoDAO pedidoDAO = new MySQLPedidoDAO();
        // Necesitas instancias de los DAOs de Cliente y Artículo
        ClienteDAO clienteDAO = new MySQLClienteDAO();
        ArticuloDAO articuloDAO = new MySQLArticuloDAO();

        try {
            // --- 1. PREPARACIÓN E INSERCIÓN DE CLAVES FORÁNEAS (NUEVO PASO) ---
            // Crear objetos de prueba (usando emails/códigos únicos, ej: v3)
            Cliente clienteDePrueba = new ClienteEstandar(
                    "luisa_v3@test.com",
                    "Luisa Sanz",
                    "Avenida 20",
                    "55559555L"
            );
            Articulo articuloDePrueba = new Articulo(
                    "A101_V3",
                    "Monitor 27 Pulgadas",
                    250.00,
                    10.00,
                    60
            );

            // **INSERTAR EN LA BASE DE DATOS**
            // Si estos clientes/artículos ya existen de pruebas anteriores,
            // esta línea lanzará una excepción (y es correcto que lo haga si la prueba no maneja la limpieza).
            // Si no quieres que falle, usa datos frescos.
            try {
                clienteDAO.insertar(clienteDePrueba);
                articuloDAO.insertar(articuloDePrueba);
            } catch (Exception e) {
                System.out.println("Advertencia: Cliente/Artículo ya existen. Usando los datos existentes.");
            }

            // --- 2. CREACIÓN DEL PEDIDO (Transaccional) ---
            Pedido nuevoPedido = new Pedido(
                    clienteDePrueba,
                    articuloDePrueba,
                    0,
                    2,
                    LocalDateTime.now(),
                    articuloDePrueba.getGastoEnvio(),
                    articuloDePrueba.getTiempoPreparacionMin()
            );

            System.out.println("--- 3. Insertando Pedido Transaccional ---");
            pedidoDAO.insertar(nuevoPedido);
            // ... (resto de la prueba) ...

        } catch (Exception e) {
            // ... (manejo de error) ...
            e.printStackTrace();
        }
    }
}
