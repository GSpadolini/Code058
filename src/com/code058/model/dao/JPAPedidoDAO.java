package com.code058.model.dao;

import com.code058.model.Articulo;
import com.code058.model.Pedido;
import com.code058.model.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class JPAPedidoDAO implements  PedidoDAO {
    @Override
    public Pedido obtenerPorNumero(int numeroPedido) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        // El metodo find busca por la clave primaria (@Id)
        Pedido pedido = em.find(Pedido.class, numeroPedido);
        em.close();
        return pedido;
    }

    @Override
    public void insertar(Pedido pedido) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin(); // 1. INICIAR TRANSACCIÓN

            // 2. Usar el EntityManager para persistir (INSERT) el objeto
            em.persist(pedido);

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
    public List<Pedido> obtenerPendientes(String emailCliente) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Pedido> pedidos = null;

        try {
            pedidos = em.createQuery(
                            "SELECT p FROM Pedido p WHERE p.cliente.email = :email AND timestampadd(MINUTE, p.tiempoPreparacion, p.fechaPedido) > CURRENT_TIMESTAMP()", Pedido.class)
                    .setParameter("email", emailCliente) // 1. Asignamos el valor del parámetro
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener la lista de pedidos pendientes del cliente: " + emailCliente, e);
        } finally {
            em.close();
        }
        return pedidos;
    }

    @Override
    public List<Pedido> obtenerPendientes() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Pedido> pedidos = null;

        try {
            // JPQL: Usamos el campo mapeado 'fechaPedido' y 'tiempoPreparacion' para simular la lógica 'esCancelable'
            // Esto filtra pedidos donde la fecha de envío proyectada es en el futuro.
            pedidos = em.createQuery(
                            "SELECT p FROM Pedido p WHERE timestampadd(MINUTE, p.tiempoPreparacion, p.fechaPedido) > CURRENT_TIMESTAMP()", Pedido.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener la lista de pedidos pendientes.", e);
        } finally {
            em.close();
        }
        return pedidos;
    }

    @Override
    public List<Pedido> obtenerEnviados(String emailCliente) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Pedido> pedidos = null;

        try {
            pedidos = em.createQuery(
                            "SELECT p FROM Pedido p WHERE p.cliente.email = :email AND timestampadd(MINUTE, p.tiempoPreparacion, p.fechaPedido) <= CURRENT_TIMESTAMP()", Pedido.class)
                    .setParameter("email", emailCliente)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener la lista de pedidos enviados del cliente: " + emailCliente, e);
        } finally {
            em.close();
        }
        return pedidos;
    }

    @Override
    public List<Pedido> obtenerEnviados() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Pedido> pedidos = null;

        try {
            // JPQL: La fecha de envío proyectada es en el pasado (ya enviado/no cancelable).
            pedidos = em.createQuery(
                            "SELECT p FROM Pedido p WHERE timestampadd(MINUTE, p.tiempoPreparacion, p.fechaPedido) <= CURRENT_TIMESTAMP()", Pedido.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al obtener la lista de pedidos enviados.", e);
        } finally {
            em.close();
        }
        return pedidos;
    }

    @Override
    public void eliminar(int numeroPedido) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            // 1. Buscar la entidad gestionada por el EM
            Pedido pedido = em.find(Pedido.class, numeroPedido);
            if (pedido != null) {
                if (!pedido.esCancelable()) {
                    tx.rollback();
                    throw new Exception("El pedido " + numeroPedido + " no puede ser eliminado (no cancelable).");
                }

                em.remove(pedido);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error al eliminar el pedido " + numeroPedido + ": " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
