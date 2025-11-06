package com.code058.model;

import com.code058.modelo.dao.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("Conexión OK. SELECT 1 -> " + rs.getInt(1));
            } else {
                System.out.println("Conexión establecida pero SELECT 1 no devolvió filas");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }
}