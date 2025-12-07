package com.code058.modelo.dao;

import com.code058.model.Pedido;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IPedidoDAO {

    // ===== Métodos estándar =====
    int insertar(Pedido p) throws SQLException;
    boolean eliminarSiCancelable(int numeroPedido) throws SQLException;

    List<Pedido> obtenerTodosBasico() throws SQLException;
    List<Pedido> obtenerPendientes(String emailCliente) throws SQLException;
    List<Pedido> obtenerEnviados(String emailCliente) throws SQLException;
    List<Pedido> obtenerTodosFiltradosPorCliente(String emailCliente) throws SQLException;
    List<Pedido> obtenerCompletados(String emailCliente) throws SQLException;

    // ===== Métodos transaccionales =====
    int insertar(Connection conn, Pedido p) throws SQLException;
    boolean eliminarSiCancelable(Connection conn, int numeroPedido) throws SQLException;

    List<Pedido> obtenerTodosBasico(Connection conn) throws SQLException;
    List<Pedido> obtenerPendientes(Connection conn, String emailCliente) throws SQLException;
    List<Pedido> obtenerEnviados(Connection conn, String emailCliente) throws SQLException;
    List<Pedido> obtenerTodosFiltradosPorCliente(Connection conn, String emailCliente) throws SQLException;
    List<Pedido> obtenerCompletados(Connection conn, String emailCliente) throws SQLException;
}
