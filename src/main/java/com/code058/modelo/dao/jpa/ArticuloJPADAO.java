package com.code058.modelo.dao.jpa;

import com.code058.modelo.dao.IArticuloDAO;
import com.code058.model.Articulo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ArticuloJPADAO implements IArticuloDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("miPersistenceUnit");

    @Override
    public void insertar(Articulo a) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new SQLException("Error insertando artículo (JPA)", ex);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Articulo> obtenerTodos() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Articulo a ORDER BY a.codigo", Articulo.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Articulo obtenerPorCodigo(String codigo) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Articulo.class, codigo);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existeCodigo(String codigo) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(a) FROM Articulo a WHERE a.codigo = :codigo", Long.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();

            return count > 0;
        } finally {
            em.close();
        }
    }

    // Métodos con Connection → delegan
    @Override public void insertar(Connection conn, Articulo a) throws SQLException { insertar(a); }
    @Override public List<Articulo> obtenerTodos(Connection conn) throws SQLException { return obtenerTodos(); }
    @Override public Articulo obtenerPorCodigo(Connection conn, String codigo) throws SQLException { return obtenerPorCodigo(codigo); }
    @Override public boolean existeCodigo(Connection conn, String codigo) throws SQLException { return existeCodigo(codigo); }
}

