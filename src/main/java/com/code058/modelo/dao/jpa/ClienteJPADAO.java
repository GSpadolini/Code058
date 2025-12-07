package com.code058.modelo.dao.jpa;

import com.code058.modelo.dao.IClienteDAO;
import com.code058.model.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClienteJPADAO implements IClienteDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("miPersistenceUnit");

    @Override
    public void insertar(Cliente c) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new SQLException("Error insertando cliente (JPA)", ex);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existeEmail(String email) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Cliente c WHERE c.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente obtenerPorEmail(String email) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> obtenerTodos() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c ORDER BY c.email", Cliente.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Métodos con Connection → delegan
    @Override public void insertar(Connection conn, Cliente c) throws SQLException { insertar(c); }
    @Override public boolean existeEmail(Connection conn, String email) throws SQLException { return existeEmail(email); }
    @Override public Cliente obtenerPorEmail(Connection conn, String email) throws SQLException { return obtenerPorEmail(email); }
    @Override public List<Cliente> obtenerTodos(Connection conn) throws SQLException { return obtenerTodos(); }
}

