package com.code058.test;

import com.code058.model.Articulo;
import com.code058.modelo.dao.IArticuloDAO;
import com.code058.modelo.dao.jpa.ArticuloJPADAO;

public class JPAQuickTest {
    public static void main(String[] args) throws Exception {
        IArticuloDAO dao = new ArticuloJPADAO();

        Articulo a = new Articulo("X100","Prueba JPA", 9.99, 2.5, 10);
        dao.insertar(a);
        System.out.println("Insertado");

        Articulo leido = dao.obtenerPorCodigo("X100");
        System.out.println("Leído: " + leido);
    }
}
