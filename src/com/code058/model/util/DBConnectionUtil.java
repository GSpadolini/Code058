package com.code058.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    /**
     * Devuelve una nueva conexión a la base de datos MySQL, usando variables de entorno.
     * @return Objeto Connection.
     * @throws SQLException Si falla la conexión.
     */
    public static Connection getConnection() throws SQLException {
        // Validación básica: Si las variables no existen, lanzamos un error claro.
        if (URL == null || USER == null || PASSWORD == null) {
            throw new SQLException("Faltan variables de entorno (DB_URL, DB_USER, DB_PASSWORD).");
        }

        // Intenta establecer la conexión
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // (El metodo testConnection() sigue siendo el mismo)
    public static void testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            System.out.println("✅ Conexión a la base de datos exitosa usando variables de entorno.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos.");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Asegúrate de que las variables de entorno estén configuradas correctamente.");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión.");
                }
            }
        }
    }
}
