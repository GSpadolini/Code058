package com.code058;

import com.code058.model.dao.util.DBConnection;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("Conectado a: " + conn.getMetaData().getURL());
            System.out.println("Usuario: " + conn.getMetaData().getUserName());
            DBConnection.closeConnection();
        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}