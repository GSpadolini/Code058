package com.code058.model.dao;

import com.code058.model.Articulo;
import java.util.List;

public interface ArticuloDAO {
    // Definimos el contrato: lo que se debe poder hacer con los Artículos en la persistencia

    // Create
    public void insertar(Articulo articulo) throws Exception;

    // Read (por PK)
    public Articulo obtenerPorCodigo(String codigo) throws Exception;

    // Read (todos)
    public List<Articulo> obtenerTodos() throws Exception;

    public void actualizar(Articulo a) throws Exception;

    public void eliminar(String codigo) throws Exception;
}
