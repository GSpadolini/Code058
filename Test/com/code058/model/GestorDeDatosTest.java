package com.code058.model;

import com.code058.exceptions.DuplicadosException;
import org.junit.Test;

import static org.junit.Assert.*;

public class GestorDeDatosTest {

    @Test
    public void testAanadirArticuloCorrectamente() throws Exception { // <<-- throws Exception
        GestorDeDatos gestor = new GestorDeDatos();
        Articulo articulo = new Articulo("A1", "Producto test", 10.0, 2.0, 5);

        // 1. Insertar a través del método de negocio (DAO)
        gestor.anadirArticulo(articulo);

        // 2. Verificar que el artículo se ha añadido (Lectura individual a través del DAO)
        // Sustituye: assertTrue(gestor.getArticulos().containsKey("A1"));
        assertTrue(gestor.getArticulo("A1") != null); // <<-- Corregido
    }

    @Test(expected = Exception.class) // <<-- Esperar la excepción genérica del DAO
    public void testAanadirArticuloDuplicadoExcepcion() throws Exception {
        GestorDeDatos gestor = new GestorDeDatos();
        Articulo articulo = new Articulo("A1", "Producto test", 10.0, 2.0, 5);

        // Primer añadido - válido
        gestor.anadirArticulo(articulo);

        // Secundo intento, lanza excepción de clave primaria duplicada del DAO
        gestor.anadirArticulo(articulo);
    }
}

