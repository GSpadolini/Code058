package com.code058.modelo.dao.jpa;

import com.code058.modelo.dao.IPedidoDAO;
import com.code058.model.Articulo;
import com.code058.model.Pedido;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoJPADAO implements IPedidoDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("miPersistenceUnit");

    @Override
    public int insertar(Pedido p) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.flush(); // forzar asignación de @GeneratedValue
            em.getTransaction().commit();
            return p.getNumeroPedido();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new SQLException("Error insertando pedido (JPA)", ex);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean eliminarSiCancelable(int numeroPedido) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Pedido p = em.find(Pedido.class, numeroPedido);
            if (p == null) {
                em.getTransaction().commit();
                return false;
            }

            // Forzar carga del artículo, ya que fetch = LAZY
            Articulo art = p.getArticulo();
            int tiempoPreparacion = 0;

            if (art != null) {
                // usa la propiedad que tienes en Articulo
                tiempoPreparacion = art.getTiempoPreparacionMin();
            } else {
                // si no hay artículo (raro), usa el valor almacenado en Pedido (si existe)
                tiempoPreparacion = p.getTiempoPreparacion();
            }

            LocalDateTime fechaPedido = p.getFechaPedido();
            if (fechaPedido == null) {
                em.getTransaction().commit();
                return false;
            }

            long minutos = Duration.between(fechaPedido, LocalDateTime.now()).toMinutes();

            if (minutos < tiempoPreparacion) {
                // cancelable: eliminar
                em.remove(p);
                em.getTransaction().commit();
                return true;
            } else {
                // no cancelable
                em.getTransaction().rollback();
                return false;
            }

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new SQLException("Error eliminando pedido (JPA)", ex);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> obtenerTodosBasico() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pedido p " +
                                    "JOIN FETCH p.cliente c " +
                                    "LEFT JOIN FETCH p.articulo a " +
                                    "ORDER BY p.numeroPedido", Pedido.class)
                    .getResultList();
        } catch (Exception ex) {
            throw new SQLException("Error obteniendo pedidos (JPA)", ex);
        } finally {
            em.close();
        }
    }

    // --- Métodos de filtrado por estado / cliente (ejemplos, adáptalos si cambias nombres) ---

    @Override
    public List<Pedido> obtenerPendientes(String emailCliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String base = "SELECT p FROM Pedido p JOIN FETCH p.cliente c JOIN FETCH p.articulo a " +
                    "WHERE FUNCTION('TIMESTAMPDIFF', MINUTE, p.fechaPedido, CURRENT_TIMESTAMP) < a.tiempoPreparacionMin";
            if (emailCliente != null) {
                return em.createQuery(base + " AND p.cliente.email = :email", Pedido.class)
                        .setParameter("email", emailCliente)
                        .getResultList();
            } else {
                return em.createQuery(base, Pedido.class).getResultList();
            }
        } catch (Exception ex) {
            throw new SQLException("Error obteniendo pedidos pendientes (JPA)", ex);
        } finally {
            em.close();
        }
    }


    @Override
    public List<Pedido> obtenerEnviados(String emailCliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT p FROM Pedido p " +
                    "LEFT JOIN FETCH p.articulo a " +
                    "LEFT JOIN FETCH p.cliente c " +
                    "WHERE TIMESTAMPDIFF(MINUTE, p.fechaPedido, CURRENT_TIMESTAMP) >= COALESCE(a.tiempoPreparacionMin, 0) " +
                    "ORDER BY p.numeroPedido";
            if (emailCliente != null) {
                jpql = "SELECT DISTINCT p FROM Pedido p " +
                        "LEFT JOIN FETCH p.articulo a " +
                        "LEFT JOIN FETCH p.cliente c " +
                        "WHERE TIMESTAMPDIFF(MINUTE, p.fechaPedido, CURRENT_TIMESTAMP) >= COALESCE(a.tiempoPreparacionMin, 0) " +
                        "AND p.cliente.email = :email " +
                        "ORDER BY p.numeroPedido";
                return em.createQuery(jpql, Pedido.class)
                        .setParameter("email", emailCliente)
                        .getResultList();
            } else {
                return em.createQuery(jpql, Pedido.class).getResultList();
            }
        } catch (Exception ex) {
            throw new SQLException("Error obteniendo pedidos enviados (JPA)", ex);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> obtenerTodosFiltradosPorCliente(String emailCliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT p FROM Pedido p " +
                    "LEFT JOIN FETCH p.articulo a " +
                    "LEFT JOIN FETCH p.cliente c " +
                    "WHERE p.cliente.email = :email " +
                    "ORDER BY p.numeroPedido";
            return em.createQuery(jpql, Pedido.class)
                    .setParameter("email", emailCliente)
                    .getResultList();
        } catch (Exception ex) {
            throw new SQLException("Error obteniendo pedidos filtrados por cliente (JPA)", ex);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> obtenerCompletados(String emailCliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT p FROM Pedido p " +
                    "LEFT JOIN FETCH p.articulo a " +
                    "LEFT JOIN FETCH p.cliente c " +
                    "WHERE TIMESTAMPDIFF(MINUTE, p.fechaPedido, CURRENT_TIMESTAMP) >= COALESCE(a.tiempoPreparacionMin, 0) " +
                    "ORDER BY p.numeroPedido";
            if (emailCliente != null) {
                jpql = "SELECT DISTINCT p FROM Pedido p " +
                        "LEFT JOIN FETCH p.articulo a " +
                        "LEFT JOIN FETCH p.cliente c " +
                        "WHERE TIMESTAMPDIFF(MINUTE, p.fechaPedido, CURRENT_TIMESTAMP) >= COALESCE(a.tiempoPreparacionMin, 0) " +
                        "AND p.cliente.email = :email " +
                        "ORDER BY p.numeroPedido";
                return em.createQuery(jpql, Pedido.class)
                        .setParameter("email", emailCliente)
                        .getResultList();
            } else {
                return em.createQuery(jpql, Pedido.class).getResultList();
            }
        } catch (Exception ex) {
            throw new SQLException("Error obteniendo pedidos completados (JPA)", ex);
        } finally {
            em.close();
        }
    }

    // ===== Métodos transaccionales con Connection: delegamos a las versiones JPA para no romper la interfaz =====
    @Override public int insertar(Connection conn, Pedido p) throws SQLException { return insertar(p); }
    @Override public boolean eliminarSiCancelable(Connection conn, int numeroPedido) throws SQLException { return eliminarSiCancelable(numeroPedido); }
    @Override public List<Pedido> obtenerTodosBasico(Connection conn) throws SQLException { return obtenerTodosBasico(); }
    @Override public List<Pedido> obtenerPendientes(Connection conn, String emailCliente) throws SQLException { return obtenerPendientes(emailCliente); }
    @Override public List<Pedido> obtenerEnviados(Connection conn, String emailCliente) throws SQLException { return obtenerEnviados(emailCliente); }
    @Override public List<Pedido> obtenerTodosFiltradosPorCliente(Connection conn, String emailCliente) throws SQLException { return obtenerTodosFiltradosPorCliente(emailCliente); }
    @Override public List<Pedido> obtenerCompletados(Connection conn, String emailCliente) throws SQLException { return obtenerCompletados(emailCliente); }
}
