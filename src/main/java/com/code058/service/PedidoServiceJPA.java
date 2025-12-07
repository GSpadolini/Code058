package com.code058.service;

import com.code058.jpa.JPAUtil;
import com.code058.model.Pedido;
import jakarta.persistence.EntityManager;

public class PedidoServiceJPA {

    public int crearPedido(Pedido pedido) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // comprobar existencia
            if (em.find(pedido.getCliente().getClass(), pedido.getCliente().getEmail()) == null) {
                throw new Exception("Cliente no existe");
            }
            if (em.find(pedido.getArticulo().getClass(), pedido.getArticulo().getCodigo()) == null) {
                throw new Exception("Articulo no existe");
            }

            em.persist(pedido); // asigna numeroPedido automáticamente
            em.getTransaction().commit();
            return pedido.getNumeroPedido();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

