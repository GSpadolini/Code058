package com.code058.modelo.dao;

import com.code058.model.Articulo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface  IArticuloDAO {
    //Operaciones que abren/cierran su propria Connection

    List<Articulo> obtenerTodos() throws SQLException;
    void insertar(Articulo a) throws SQLException;
    Articulo obtenerPorCodigo(String codigo) throws SQLException;
    boolean existeCodigo(String codigo) throws SQLException;

    // Versiones que usan una Connection externa (para poder agrupar en transacciones)

    List<Articulo> obtenerTodos(Connection conn) throws SQLException;
    void insertar(Connection conn, Articulo a) throws SQLException;
    Articulo obtenerPorCodigo(Connection conn, String codigo) throws SQLException;
    boolean existeCodigo(Connection conn, String codigo) throws SQLException;
}
