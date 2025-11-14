package com.code058.model.dao.mysql;

import com.code058.exceptions.PedidoNoCancelableException;
import com.code058.model.Articulo;
import com.code058.model.Cliente;
import com.code058.model.Pedido;
import com.code058.model.dao.ArticuloDAO;
import com.code058.model.dao.ClienteDAO;
import com.code058.model.dao.PedidoDAO;
import com.code058.model.factory.DAOFactory;
import com.code058.model.util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySQLPedidoDAO implements PedidoDAO {
    private static final String INSERT_SQL =
            "INSERT INTO pedido (cliente_email, articulo_codigo, unidades, fecha_hora, gasto_envio, tiempo_preparacion_min) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_PK_SQL =
            "SELECT numero_pedido, cliente_email, articulo_codigo, unidades, fecha_hora, gasto_envio, tiempo_preparacion_min " +
                    "FROM pedido WHERE numero_pedido = ?";

    private static final String DELETE_SQL =
            "DELETE FROM pedido WHERE numero_pedido = ?";

    @Override
    public Pedido obtenerPorNumero(int numeroPedido) throws Exception {
        Pedido pedido = null;

        // Necesitamos DAOs para recuperar los objetos Cliente y Articulo
        // **Nota:** Esto NO es la mejor práctica (acoplamiento fuerte), pero es simple para la prueba.
        // Lo corregiremos con el patrón Factory más adelante.
        ClienteDAO clienteDAO = DAOFactory.getClienteDAO();
        ArticuloDAO articuloDAO = DAOFactory.getArticuloDAO();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PK_SQL)) {

            ps.setInt(1, numeroPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    // 1. Leer las Claves Foráneas y Datos Simples
                    String clienteEmail = rs.getString("cliente_email");
                    String articuloCodigo = rs.getString("articulo_codigo");

                    int numPedido = rs.getInt("numero_pedido");
                    int cantidad = rs.getInt("unidades");

                    // Leer DATETIME como Timestamp y convertir a LocalDateTime
                    java.sql.Timestamp fechaTs = rs.getTimestamp("fecha_hora");
                    java.time.LocalDateTime fechaHora = fechaTs.toLocalDateTime();

                    double gastoEnvio = rs.getDouble("gasto_envio");
                    int tiempoPreparacion = rs.getInt("tiempo_preparacion_min");

                    // 2. Recuperar Objetos de Negocio completos usando otros DAOs
                    Cliente cliente = clienteDAO.obtenerPorEmail(clienteEmail);
                    Articulo articulo = articuloDAO.obtenerPorCodigo(articuloCodigo);

                    if (cliente == null || articulo == null) {
                        throw new SQLException("Error de integridad: Cliente o Artículo asociado al pedido no existe.");
                    }

                    // 3. Crear el objeto Pedido completo
                    pedido = new Pedido(cliente, articulo, numPedido, cantidad, fechaHora, gastoEnvio, tiempoPreparacion);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error DAO al obtener pedido por número: " + e.getMessage(), e);
        }

        return pedido;
    }

    @Override
    public void insertar(Pedido pedido) throws Exception {
        Connection conn = null;
        ResultSet rsKeys = null; // Nuevo ResultSet para las claves generadas

        try {
            conn = DBConnectionUtil.getConnection();
            conn.setAutoCommit(false); // INICIO DE LA TRANSACCIÓN

            // 1. SOLICITAR CLAVES GENERADAS al preparar la sentencia
            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {

                // 1. Mapeo de Parámetros (igual que antes)
                ps.setString(1, pedido.getCliente().getEmail());
                ps.setString(2, pedido.getArticulo().getCodigo());
                ps.setInt(3, pedido.getCantidad());
                ps.setTimestamp(4, java.sql.Timestamp.valueOf(pedido.getFechaPedido()));
                ps.setDouble(5, pedido.getGastoEnvio());
                ps.setInt(6, pedido.getTiempoPreparacion());

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Error al insertar pedido: 0 filas afectadas.");
                }

                // 2. OBTENER EL ID GENERADO
                rsKeys = ps.getGeneratedKeys();
                if (rsKeys.next()) {
                    // 3. ACTUALIZAR EL OBJETO PEDIDO CON EL ID REAL
                    int newId = rsKeys.getInt(1); // El ID es la primera columna
                    pedido.setNumeroPedido(newId); // <<-- ¡Actualiza el objeto Java!
                } else {
                    throw new SQLException("Error al obtener el número de pedido generado.");
                }
            }

            conn.commit(); // CONFIRMACIÓN EXITOSA

        } catch (SQLException e) {
            // ... (código de rollback)
            if (conn != null) { conn.rollback(); }
            throw new Exception("Error DAO al insertar pedido en la BBDD. Causa: " + e.getMessage(), e);

        } finally {
            // 4. CERRAR EL NUEVO RESULTADO DE CLAVES
            if (rsKeys != null) {
                try { rsKeys.close(); } catch (SQLException ignore) {}
            }
            // ... (cierre de conexión, que ya tenías)
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar la conexión: " + closeEx.getMessage());
                }
            }
        }
    }


    @Override
    public List<Pedido> obtenerPendientes(String emailCliente) throws Exception {
        return null; // Tarea pendiente
    }

    @Override
    public List<Pedido> obtenerEnviados(String emailCliente) throws Exception {
        return null; // Tarea pendiente
    }

    @Override
    public void eliminar(int numeroPedido) throws Exception {

        // --- LÓGICA DE NEGOCIO (FUERA DE LA TRANSACCIÓN) ---
        Pedido pedido = obtenerPorNumero(numeroPedido);
        if (pedido == null) {
            throw new Exception("El pedido con número " + numeroPedido + " no existe.");
        }

        if (!pedido.esCancelable()) {
            // Usamos una excepción específica de negocio (asegúrate de que existe en tu paquete exceptions)
            throw new PedidoNoCancelableException("El pedido ya ha sido enviado (tiempo de preparación agotado).");
        }
        // ----------------------------------------------------

        Connection conn = null;

        try {
            conn = DBConnectionUtil.getConnection();
            conn.setAutoCommit(false); // INICIO TRANSACCIÓN

            try (PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
                ps.setInt(1, numeroPedido);

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Error al eliminar pedido: 0 filas afectadas después de la verificación.");
                }
            }

            conn.commit(); // CONFIRMACIÓN
            System.out.println("✅ Pedido N°" + numeroPedido + " eliminado transaccionalmente.");

        } catch (SQLException e) {
            // ... (código de rollback y re-lanzamiento) ...
            if (conn != null) conn.rollback();
            throw new Exception("Error DAO al eliminar pedido: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try {
                    // 1. Restablecer el auto-commit al estado por defecto
                    conn.setAutoCommit(true);
                    // 2. Cerrar la conexión
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar la conexión: " + closeEx.getMessage());
                }
            }
        }
    }
}
