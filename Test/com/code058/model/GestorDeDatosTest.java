package com.code058.model;

import com.code058.exceptions.DuplicadosException;
import org.junit.Test;

import static org.junit.Assert.*;

public class GestorDeDatosTest {

    @Test
    public void testAanadirArticuloCorrectamente() throws DuplicadosException{
        GestorDeDatos gestor = new GestorDeDatos();
        Articulo articulo = new Articulo("A1", "Producto test", 10.0, 2.0, 5);

        gestor.anadirArticulo(articulo);

        //Verificamos que el artículo se ha añadido correctamente
        assertTrue(gestor.getArticulos().containsKey("A1"));
    }

    @Test(expected = DuplicadosException.class)
    public void testAanadirArticuloDuplicadoExcepcion() throws DuplicadosException {
        GestorDeDatos gestor = new GestorDeDatos();
        Articulo articulo = new Articulo("A1", "Producto test", 10.0, 2.0, 5);

        //Primer añadido - válido
        gestor.anadirArticulo(articulo);

        //Secundo intento, lanza excepción
        gestor.anadirArticulo(articulo);
    }
}

