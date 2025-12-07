package com.code058.modelo.dao;

import java.util.Locale;

/**
 * DAOFactory que devuelve interfaces (IArticuloDAO, IClienteDAO, IPedidoDAO).
 */
public class DAOFactory {

    public enum Tipo { JDBC, JPA }

    private static Tipo modo = detectDefault();

    private static Tipo detectDefault() {
        String prop = System.getProperty("dao.impl");
        if (prop == null || prop.isBlank()) {
            prop = System.getenv("DAO_IMPL");
        }
        if (prop != null) {
            try {
                return Tipo.valueOf(prop.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {}
        }
        return Tipo.JDBC;
    }

    public static void configurar(Tipo t) { modo = t; }
    public static Tipo obtenerModo() { return modo; }

    public static IArticuloDAO getArticuloDAO() {
        return (modo == Tipo.JPA)
                ? new com.code058.modelo.dao.jpa.ArticuloJPADAO()
                : new com.code058.modelo.dao.ArticuloDAO(); // <- usa paquete actual
    }

    public static IClienteDAO getClienteDAO() {
        return (modo == Tipo.JPA)
                ? new com.code058.modelo.dao.jpa.ClienteJPADAO()
                : new com.code058.modelo.dao.ClienteDAO(); // <- usa paquete actual
    }

    public static IPedidoDAO getPedidoDAO() {
        return (modo == Tipo.JPA)
                ? new com.code058.modelo.dao.jpa.PedidoJPADAO()
                : new com.code058.modelo.dao.PedidoDAO(); // <- usa paquete actual
    }
}
