package com.code058.model.dao;

import com.code058.model.Cliente;
import com.code058.model.util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class JPAClienteDAO implements ClienteDAO {
    @Override
    public void insertar(Cliente cliente) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            // Persist funciona con Cliente (base) o ClienteEstandar/Premium (subclases)
            em.persist(cliente);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error al insertar el cliente: " + cliente.getEmail(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente obtenerPorEmail(String email) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Cliente cliente = null;
        try {
            // Busca por la clave primaria (@Id)
            cliente = em.find(Cliente.class, email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener el cliente con email: " + email, e);
        } finally {
            em.close();
        }
        return cliente;
    }

    @Override
    public List<Cliente> obtenerTodos() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Cliente> clientes = null;
        try {
            // JPQL: Selecciona todas las entidades Cliente (incluye subtipos)
            clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener la lista de clientes.", e);
        } finally {
            em.close();
        }
        return clientes;
    }

    @Override
    public List<Cliente> obtenerEstandar() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Cliente> clientesEstandar = null;

        try {
            // JPQL: Consultamos la entidad específica ClienteEstandar
            // JPA se encarga de filtrar por el valor del DiscriminatorValue ("ESTANDAR")
            clientesEstandar = em.createQuery("SELECT c FROM ClienteEstandar c", Cliente.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener la lista de clientes Estándar.", e);
        } finally {
            em.close();
        }
        return clientesEstandar;
    }

    @Override
    public List<Cliente> obtenerPremium() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Cliente> clientesPremium = null;

        try {
            // JPQL: Consultamos la entidad específica ClientePremium
            // JPA se encarga de filtrar por el valor del DiscriminatorValue ("PREMIUM")
            clientesPremium = em.createQuery("SELECT c FROM ClientePremium c", Cliente.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener la lista de clientes Premium.", e);
        } finally {
            em.close();
        }
        return clientesPremium;
    }
}
