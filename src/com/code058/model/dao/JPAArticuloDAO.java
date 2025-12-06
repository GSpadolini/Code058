package com.code058.model.dao;

import com.code058.model.Articulo;
import com.code058.model.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class JPAArticuloDAO implements ArticuloDAO {
    @Override
    public void insertar(Articulo articulo) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin(); // 1. INICIAR TRANSACCIÓN

            // 2. Usar el EntityManager para persistir (INSERT) el objeto
            em.persist(articulo);

            tx.commit(); // 3. CONFIRMAR (ejecutar) la operación en la BD
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Si algo falla, deshacer
            }
            e.printStackTrace();
        } finally {
            em.close(); // 4. CERRAR el EntityManager
        }
    }

    @Override
    public Articulo obtenerPorCodigo(String codigo) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        // El metodo find busca por la clave primaria (@Id)
        Articulo articulo = em.find(Articulo.class, codigo);
        em.close();
        return articulo;
    }

    @Override
    public List<Articulo> obtenerTodos() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Articulo> articulos = null;

        try {
            // Usamos createQuery con JPQL: "SELECT a FROM Articulo a"
            // 'Articulo' es el nombre de la clase Java (@Entity)
            articulos = em.createQuery("SELECT a FROM Articulo a", Articulo.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener la lista de artículos.", e);
        } finally {
            em.close();
        }
        return articulos;
    }

    @Override
    public void actualizar(Articulo articulo) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            // 'merge' se usa para actualizar una entidad
            em.merge(articulo);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error al actualizar el artículo con código: " + articulo.getCodigo(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(String codigo) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            // 1. Buscar la entidad por su PK para obtener la referencia gestionada
            Articulo articulo = em.find(Articulo.class, codigo);

            if (articulo != null) {
                // 2. Eliminar la entidad gestionada
                em.remove(articulo);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error al eliminar el artículo con código: " + codigo, e);
        } finally {
            em.close();
        }
    }

}
