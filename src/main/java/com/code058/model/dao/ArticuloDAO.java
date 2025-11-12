package com.code058.model.dao;

import com.code058.model.Articulo;
import java.sql.SQLException;
import java.util.Map;

public interface ArticuloDAO {
    void insertar(Articulo articulo) throws SQLException;
    Map<String, Articulo> listarTodos() throws SQLException;
    boolean existeCodigo(String codigo) throws SQLException;
}
