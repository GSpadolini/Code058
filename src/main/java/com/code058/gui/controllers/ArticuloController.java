package com.code058.gui.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller robusto que usa reflexión para ser tolerante a diferencias
 * en paquetes/nombres de métodos/propiedades entre distintas versiones del proyecto.
 *
 * Correción: evita usar List<?> para añadir elementos; usamos List<Object> y copiamos los elementos.
 */
public class ArticuloController {

    @FXML
    private TableView<Object> tablaArticulos;

    @FXML
    private TableColumn<Object, String> colId;

    @FXML
    private TableColumn<Object, String> colNombre;

    @FXML
    private TableColumn<Object, String> colPrecio;

    private Object articuloDAO;

    @FXML
    public void initialize() {
        try {
            // 1) Intentar obtener una instancia de DAOFactory (prueba varios nombres comunes)
            Class<?> daoFactoryClass = tryLoadClass(
                    "com.code058.dao.DAOFactory",
                    "com.code058.modelo.dao.DAOFactory",
                    "com.code058.modelo.DAOFactory",
                    "com.code058.modelo.factory.DAOFactory"
            );

            Object daoFactoryInstance = tryGetFactoryInstance(daoFactoryClass);

            // 2) Intentar obtener el IArticuloDAO (invocando métodos candidatos)
            articuloDAO = tryGetArticuloDAO(daoFactoryInstance);

            if (articuloDAO == null) {
                System.err.println("[ArticuloController] No se pudo obtener articuloDAO. Comprueba DAOFactory y nombres de paquetes.");
            } else {
                System.out.println("[ArticuloController] articuloDAO obtenido: " + articuloDAO.getClass().getName());
            }

            // 3) Configurar columnas: usamos lambdas que obtienen el valor vía reflexión
            colId.setCellValueFactory(cell -> new SimpleStringProperty(
                    stringValueOfFieldOrGetter(cell.getValue(), "id", "idArticulo", "articuloId", "getId")
            ));

            colNombre.setCellValueFactory(cell -> new SimpleStringProperty(
                    stringValueOfFieldOrGetter(cell.getValue(), "nombre", "nombreArticulo", "getNombre", "getDescripcion")
            ));

            colPrecio.setCellValueFactory(cell -> new SimpleStringProperty(
                    stringValueOfFieldOrGetter(cell.getValue(), "precio", "precioUnitario", "precioVenta", "getPrecio")
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cargarArticulos() {
        try {
            if (articuloDAO == null) {
                System.err.println("[ArticuloController] No se ha obtenido articuloDAO. Revisa DAOFactory/paquetes.");
                return;
            }

            // Intentar invocar métodos que devuelven lista de artículos
            Object result = tryInvokeMethodAny(articuloDAO,
                    new String[]{"findAll", "listar", "listarTodos", "getAll", "findAllArticulos", "findAllItems"});

            if (result == null) {
                System.err.println("[ArticuloController] El DAO devolvió null al invocar findAll/listar...");
                return;
            }

            // --- CORRECCIÓN: usar List<Object> y copiar elementos ---
            List<Object> listaObjetos = new ArrayList<>();

            if (result instanceof List) {
                List<?> original = (List<?>) result;
                for (Object o : original) {
                    listaObjetos.add(o);
                }
            } else {
                // si el DAO devuelve un único objeto, lo añadimos directamente
                listaObjetos.add(result);
            }

            ObservableList<Object> data = FXCollections.observableArrayList();
            data.addAll(listaObjetos);

            tablaArticulos.setItems(data);

            System.out.println("[ArticuloController] Cargados " + data.size() + " artículos en la tabla.");

        } catch (Exception e) {
            System.err.println("[ArticuloController] Error al cargar artículos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------------- helper reflection ----------------

    private static Class<?> tryLoadClass(String... names) {
        for (String n : names) {
            try {
                Class<?> c = Class.forName(n);
                System.out.println("[ArticuloController] Clase encontrada: " + n);
                return c;
            } catch (Throwable ignored) { }
        }
        return null;
    }

    private static Object tryGetFactoryInstance(Class<?> factoryClass) {
        if (factoryClass == null) return null;
        // probar métodos estáticos comunes: getInstance, getFactory, getDAOFactory, instance
        String[] candidates = {"getInstance", "getFactory", "getDAOFactory", "getSingleton", "instance"};
        for (String m : candidates) {
            try {
                Method method = factoryClass.getMethod(m);
                Object inst = method.invoke(null);
                System.out.println("[ArticuloController] Factory instance via " + m + ": " + (inst != null ? inst.getClass().getName() : "null"));
                return inst;
            } catch (Throwable ignored) { }
        }
        // si no hay método estático, intentar constructor público sin args
        try {
            Object inst = factoryClass.getDeclaredConstructor().newInstance();
            System.out.println("[ArticuloController] Factory instance via constructor: " + inst.getClass().getName());
            return inst;
        } catch (Throwable ignored) { }
        return null;
    }

    private static Object tryGetArticuloDAO(Object factoryInstance) {
        if (factoryInstance == null) return null;
        Class<?> cls = factoryInstance.getClass();
        // métodos que suelen devolver el DAO
        String[] daoGetters = {"getArticuloDAO", "getArticuloDao", "getIArticuloDAO", "getArticulosDAO", "createArticuloDAO", "getDAO"};
        for (String m : daoGetters) {
            try {
                Method method = cls.getMethod(m);
                Object dao = method.invoke(factoryInstance);
                System.out.println("[ArticuloController] DAO obtenido via " + m + ": " + (dao != null ? dao.getClass().getName() : "null"));
                return dao;
            } catch (Throwable ignored) { }
        }
        // probar métodos genéricos que devuelven DAOs por clase/nombre (menos probable)
        try {
            Method m = cls.getMethod("getDAO", Class.class);
            // intentar con una clase Articulo si existe
            Class<?> artClass = tryLoadClass("com.code058.model.Articulo", "com.code058.modelo.Articulo", "com.code058.modelo.entidades.Articulo");
            if (artClass != null) {
                Object dao = m.invoke(factoryInstance, artClass);
                System.out.println("[ArticuloController] DAO obtenido via getDAO(Class): " + (dao != null ? dao.getClass().getName() : "null"));
                return dao;
            }
        } catch (Throwable ignored) { }
        return null;
    }

    private static Object tryInvokeMethodAny(Object target, String[] methodNames) {
        if (target == null) return null;
        Class<?> cls = target.getClass();
        for (String name : methodNames) {
            try {
                Method m = cls.getMethod(name);
                Object r = m.invoke(target);
                System.out.println("[ArticuloController] Método invocado: " + name + " -> " + (r != null ? r.getClass().getName() : "null"));
                return r;
            } catch (Throwable ignored) { }
        }
        return null;
    }

    private static String stringValueOfFieldOrGetter(Object obj, String... candidates) {
        if (obj == null) return "";
        Class<?> cls = obj.getClass();

        // 1) intentar getters: getX / isX
        for (String c : candidates) {
            String cap = capitalize(c);
            String[] gm = {"get" + cap, "is" + cap, c};
            for (String g : gm) {
                try {
                    Method method = cls.getMethod(g);
                    Object val = method.invoke(obj);
                    if (val != null) return val.toString();
                } catch (Throwable ignored) { }
            }
        }

        // 2) intentar acceso directo a campo
        for (String c : candidates) {
            try {
                Field f = cls.getDeclaredField(c);
                f.setAccessible(true);
                Object val = f.get(obj);
                if (val != null) return val.toString();
            } catch (Throwable ignored) { }
        }

        // 3) fallback: toString()
        return obj.toString();
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
