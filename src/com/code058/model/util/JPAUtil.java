package com.code058.model.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "CursoUOC";

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        // --- 1. LEER LAS PROPIEDADES DIRECTAMENTE DEL SISTEMA ---
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        properties.put("hibernate.connection.url", System.getenv("DB_URL"));
        properties.put("hibernate.connection.username", System.getenv("DB_USER"));
        properties.put("hibernate.connection.password", System.getenv("DB_PASSWORD"));

        // C3P0 necesita que estas propiedades también estén mapeadas para el pool:
        properties.put("hibernate.c3p0.driverClass", "com.mysql.cj.jdbc.Driver");
        properties.put("hibernate.c3p0.jdbcUrl", System.getenv("DB_URL"));
        properties.put("hibernate.c3p0.user", System.getenv("DB_USER"));
        properties.put("hibernate.c3p0.password", System.getenv("DB_PASSWORD"));

        // --- 2. INICIALIZAR JPA INYECTANDO LAS PROPIEDADES ---
        try {
            // Pasamos las propiedades al método de inicialización
            return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
        } catch (Exception ex) {
            System.err.println("La creación del EntityManagerFactory falló.");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Proporciona la fábrica para obtener instancias de EntityManager.
     * @return EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }

    /**
     * Cierra la fábrica de EntityManager cuando la aplicación finaliza.
     */
    public static void shutdown() {
        if (ENTITY_MANAGER_FACTORY != null) {
            ENTITY_MANAGER_FACTORY.close();
            System.out.println("EntityManagerFactory cerrada.");
        }
    }

    public static void testJPAConnection() {
        System.out.println("--- Iniciando Test de Conexión JPA ---");
        try {
            // Intentar obtener una instancia de EntityManagerFactory
            EntityManagerFactory emf = getEntityManagerFactory();

            // Si llega aquí, la configuración es válida
            System.out.println("✅ JPA Factory inicializada con éxito.");

            // Opcional: Cerrar la fábrica si no queremos que se mantenga activa solo por el test
            // emf.close(); // NO: La necesitamos activa, así que la dejamos abierta.

        } catch (ExceptionInInitializerError e) {
            // Captura el error lanzado por buildEntityManagerFactory
            System.err.println("❌ ERROR: Fallo al crear el EntityManagerFactory.");
            System.err.println("Revisar persistence.xml y el Classpath.");
            // e.printStackTrace(); // Ya está impresa por el catch original en buildEntityManagerFactory
            throw e; // Relanzar para detener el programa si la persistencia es crítica
        }
        System.out.println("-------------------------------------");
    }
}
