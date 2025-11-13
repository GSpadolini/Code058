package com.code058.modelo.dao.sql;

import com.code058.model.Articulo;
import com.code058.modelo.dao.ArticuloDAO;
import com.code058.modelo.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class MySQLArticuloDAO implements ArticuloDAO {

    // Sentencia SQL para la inserción (IMPORTANTE: usamos ? para PreparedStatement)
    private static final String INSERT_SQL =
            "INSERT INTO articulo (codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_PK_SQL =
            "SELECT codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min " +
                    "FROM articulo WHERE codigo = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM articulo";

    private static final String UPDATE_SQL =
            "UPDATE articulo SET descripcion = ?, precio_venta = ?, gastos_envio = ?, tiempo_preparacion_min = ? WHERE codigo = ?";

    private static final String DELETE_SQL =
            "DELETE FROM articulo WHERE codigo = ?";

    @Override
    public void insertar(Articulo a) throws Exception {

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(INSERT_SQL)){

            // 3. Establecer los parámetros (evita SQL Injection)
            ps.setString(1, a.getCodigo());
            ps.setString(2, a.getDescripcion());
            ps.setDouble(3, a.getPrecioVenta());
            ps.setDouble(4, a.getGastoEnvio());
            ps.setInt(5, a.getTiempoPreparacionMin());

            // 4. Ejecutar la sentencia DML
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                // Manejar el caso de que la inserción falle
                throw new SQLException("No se pudo insertar el artículo.");
            }

        } catch (SQLException e) {
            // Aquí se pueden encapsular las SQLException en una excepción de negocio si es necesario
            throw new Exception("Error al insertar artículo en la BBDD: " + e.getMessage(), e);
        }
    }

    @Override
    public Articulo obtenerPorCodigo(String codigo) throws Exception {

        Articulo articulo = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PK_SQL)) {

            // 1. Establecer el parámetro del código que estamos buscando
            ps.setString(1, codigo);

            // 2. Ejecutar la consulta SELECT. El resultado se almacena en 'rs' (ResultSet).
            try (ResultSet rs = ps.executeQuery()) {

                // 3. Procesar el ResultSet
                // El metodo next() mueve el cursor a la siguiente fila.
                // Para una búsqueda por PK, solo esperamos UNA fila (o ninguna).
                if (rs.next()) {
                    // 4. Recrear el objeto Articulo con los datos de la base de datos
                    articulo = new Articulo(
                            rs.getString("codigo"), // Columna 1 o nombre de columna
                            rs.getString("descripcion"),
                            rs.getDouble("precio_venta"),
                            rs.getDouble("gastos_envio"),
                            rs.getInt("tiempo_preparacion_min")
                    );
                }
                // Si rs.next() es falso, significa que no se encontró el artículo y se devuelve null (que es el valor inicial de 'articulo').
            }

        } catch (SQLException e) {
            throw new Exception("Error DAO al obtener artículo por código: " + e.getMessage(), e);
        }

        return articulo;
    }

    @Override
    public List<Articulo> obtenerTodos() throws Exception {
        List<Articulo> articulos = new ArrayList<>();

        // Usamos try-with-resources para Connection y PreparedStatement
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) { // El ResultSet también puede ir en el try-with-resources

            // 1. Iterar sobre todos los resultados
            // Mientras rs.next() devuelva true (hay otra fila), el bucle se ejecuta
            while (rs.next()) {
                // 2. Recrear el objeto Articulo
                Articulo articulo = new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion_min")
                );

                // 3. Añadir el objeto Articulo a la lista
                articulos.add(articulo);
            }

        } catch (SQLException e) {
            throw new Exception("Error DAO al obtener todos los artículos: " + e.getMessage(), e);
        }

        return articulos;
    }
    //Sabemos que solo nos piden añadir un articulo y mostrarlo, no nos piden actualizar
    // ni eliminar, pero lo implementamos para completar el CRUD(como apredizaje)
    @Override
    public void actualizar(Articulo a) throws Exception {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            // 1. Parámetros de los valores a actualizar
            ps.setString(1, a.getDescripcion());
            ps.setDouble(2, a.getPrecioVenta());
            ps.setDouble(3, a.getGastoEnvio());
            ps.setInt(4, a.getTiempoPreparacionMin());

            // 2. Parámetro de la condición (PK)
            ps.setString(5, a.getCodigo());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Error: No se pudo actualizar el artículo. Código no encontrado.");
            }
        } catch (SQLException e) {
            throw new Exception("Error DAO al actualizar artículo: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(String codigo) throws Exception {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setString(1, codigo); // Parámetro de la condición

            if (ps.executeUpdate() == 0) {
                // Nota: Podríamos no lanzar excepción si no lo encuentra y simplemente no hacer nada.
                System.out.println("Advertencia: Artículo con código " + codigo + " no encontrado para eliminar.");
            } else {
                System.out.println("Artículo con código " + codigo + " eliminado correctamente.");
            }
        } catch (SQLException e) {
            throw new Exception("Error DAO al eliminar artículo: " + e.getMessage(), e);
        }
    }
}
