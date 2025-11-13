
package com.code058.model.dao;

import com.code058.model.Pedido;
import java.sql.SQLException;
import java.util.List;

public interface PedidoDAO {
    void insertar(Pedido pedido) throws SQLException;
    List<Pedido> listarTodos() throws SQLException;
    void eliminar(int numeroPedido) throws SQLException;
}
