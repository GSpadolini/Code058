package com.code058.controller;

import com.code058.model.Articulo;
import com.code058.model.GestorDeDatos;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import com.code058.model.Cliente;
import com.code058.model.ClienteEstandar;
import com.code058.model.ClientePremium;
import javafx.scene.control.ComboBox;
import com.code058.model.Pedido;
import com.code058.exceptions.PedidoNoCancelableException; // Asegúrate de tener esta exception
import javafx.scene.control.RadioButton;

public class MainFXController {

    private GestorDeDatos modelo;

    // --- ELEMENTOS DE LA PESTAÑA ARTÍCULOS (FXML) ---
    @FXML private TableView<Articulo> tablaArticulos;
    @FXML private TableColumn<Articulo, String> colCodigo;
    @FXML private TableColumn<Articulo, String> colDescripcion;
    @FXML private TableColumn<Articulo, Double> colPrecio;
    @FXML private TableColumn<Articulo, Double> colGastos;
    @FXML private TableColumn<Articulo, Integer> colTiempo;

    @FXML private TextField txtArtCodigo;
    @FXML private TextField txtArtDescripcion;
    @FXML private TextField txtArtPrecio;
    @FXML private TextField txtArtGastos;
    @FXML private TextField txtArtTiempo;

    @FXML private TextField txtCliEmail;
    @FXML private TextField txtCliNombre;
    @FXML private TextField txtCliNif;
    @FXML private TextField txtCliDomicilio;
    @FXML private ComboBox<String> comboTipoCliente;

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colCliEmail;
    @FXML private TableColumn<Cliente, String> colCliNombre;
    @FXML private TableColumn<Cliente, String> colCliNif;
    @FXML private TableColumn<Cliente, String> colCliDomicilio;
    @FXML private TableColumn<Cliente, String> colCliTipo;
    @FXML private TableColumn<Cliente, Double> colCliCuota;

    @FXML private ComboBox<String> comboPedCliente;   // Guardaremos el Email
    @FXML private ComboBox<String> comboPedArticulo;  // Guardaremos el Código
    @FXML private TextField txtPedUnidades;

    @FXML private RadioButton radioPendientes;
    @FXML private RadioButton radioEnviados;
    @FXML private TextField txtFiltroEmail;

    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, Integer> colPedId;
    @FXML private TableColumn<Pedido, String> colPedFecha;
    @FXML private TableColumn<Pedido, String> colPedCliente;
    @FXML private TableColumn<Pedido, String> colPedArticulo;
    @FXML private TableColumn<Pedido, Integer> colPedUnidades;
    @FXML private TableColumn<Pedido, Double> colPedTotal;
    @FXML private TableColumn<Pedido, String> colPedEstado;
    @FXML private TableColumn<Pedido, Integer> colPedTiempoPre;


    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colGastos.setCellValueFactory(new PropertyValueFactory<>("gastoEnvio"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoPreparacionMin"));

        colCliEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCliNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCliNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        colCliDomicilio.setCellValueFactory(new PropertyValueFactory<>("domicilio"));
        comboTipoCliente.getItems().addAll("Estándar", "Premium");
        comboTipoCliente.getSelectionModel().selectFirst();


        colCliTipo.setCellValueFactory(cellData -> {

            if (cellData.getValue() instanceof ClientePremium) {
                return new javafx.beans.property.SimpleStringProperty("Premium");
            } else {
                return new javafx.beans.property.SimpleStringProperty("Estándar");
            }
        });

        colCliCuota.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof ClientePremium) {
                return new javafx.beans.property.SimpleObjectProperty<>(30.0);
            } else {
                return new javafx.beans.property.SimpleObjectProperty<>(0.0);
            }
        });


        colPedId.setCellValueFactory(new PropertyValueFactory<>("numeroPedido"));
        colPedFecha.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFechaPedido().toString()));


        colPedCliente.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCliente().getEmail()));


        colPedArticulo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getArticulo().getCodigo()));

        colPedUnidades.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        colPedTotal.setCellValueFactory(cell -> {
            double total = cell.getValue().getArticulo().getPrecioVenta() * cell.getValue().getCantidad();

            return new javafx.beans.property.SimpleObjectProperty<>(total);
        });

        colPedEstado.setCellValueFactory(cell -> {
            if (!cell.getValue().esCancelable()) {
                return new javafx.beans.property.SimpleStringProperty("ENVIADO");
            } else {
                return new javafx.beans.property.SimpleStringProperty("PENDIENTE");
            }
        });

        colPedTiempoPre.setCellValueFactory(new PropertyValueFactory<>("tiempoPreparacion"));

        comboPedCliente.setOnMouseClicked(e -> actualizarCombos());

    }

    @FXML
    public void accionAnadirArticulo() {
        try {
            // 1. Validar que no haya campos vacíos
            if (txtArtCodigo.getText().isEmpty() || txtArtDescripcion.getText().isEmpty()) {
                mostrarAlerta("Error", "El código y la descripción son obligatorios.");
                return;
            }
            // 2. Obtener datos y convertir números
            String codigo = txtArtCodigo.getText();
            String descripcion = txtArtDescripcion.getText();
            double precio = Double.parseDouble(txtArtPrecio.getText());
            double gastos = Double.parseDouble(txtArtGastos.getText());
            int tiempo = Integer.parseInt(txtArtTiempo.getText());

            // 3. Crear objeto y llamar al modelo
            Articulo nuevo = new Articulo(codigo, descripcion, precio, gastos, tiempo);
            modelo.anadirArticulo(nuevo); // Esto lanza excepción si ya existe o falla la BBDD

            // 4. Éxito: Limpiar campos y recargar tabla
            mostrarAlerta("Éxito", "Artículo añadido correctamente.");
            limpiarFormularioArticulos();
            cargarArticulos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Precio, Gastos y Tiempo deben ser números válidos (usa el punto '.' para decimales).");
        } catch (Exception e) {
            mostrarAlerta("Error al Guardar", "No se pudo añadir el artículo: " + e.getMessage());
        }
    }

    private void limpiarFormularioArticulos() {
        txtArtCodigo.clear();
        txtArtDescripcion.clear();
        txtArtPrecio.clear();
        txtArtGastos.clear();
        txtArtTiempo.clear();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (titulo.contains("Error")) alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public void setModelo(GestorDeDatos modelo) {
        this.modelo = modelo;
        cargarArticulos();
    }

    @FXML
    public void cargarArticulos() {
        if (modelo != null) {
            try {

                List<Articulo> lista = modelo.getArticulos();

                tablaArticulos.setItems(FXCollections.observableArrayList(lista));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void accionAnadirCliente() {
        try {
            // 1. Validar
            if (txtCliEmail.getText().isEmpty() || txtCliNombre.getText().isEmpty() || txtCliNif.getText().isEmpty()) {
                mostrarAlerta("Error", "Debes rellenar todos los campos.");
                return;
            }

            // 2. Recoger datos
            String email = txtCliEmail.getText();
            String nombre = txtCliNombre.getText();
            String nif = txtCliNif.getText();
            String domicilio = txtCliDomicilio.getText();
            String tipo = comboTipoCliente.getValue();

            // 3. Crear el objeto correcto según la herencia
            Cliente clienteNuevo;
            if ("Premium".equals(tipo)) {
                // Asumo que el constructor es (email, nombre, domicilio, nif).
                // La cuota de 30€ se gestiona internamente en la clase o en BBDD por defecto.
                clienteNuevo = new ClientePremium(email, nombre, domicilio, nif);
            } else {
                clienteNuevo = new ClienteEstandar(email, nombre, domicilio, nif);
            }

            // 4. Guardar
            modelo.anadirCliente(clienteNuevo);

            mostrarAlerta("Éxito", "Cliente guardado correctamente.");
            limpiarFormularioClientes();
            cargarClientesTodos(); // Refrescar la tabla

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el cliente: " + e.getMessage());
        }
    }

    private void limpiarFormularioClientes() {
        txtCliEmail.clear();
        txtCliNombre.clear();
        txtCliNif.clear();
        txtCliDomicilio.clear();
    }

    // --- MÉTODOS DE FILTRADO (Requisitos 2.2, 2.3, 2.4) ---

    @FXML
    public void cargarClientesTodos() {
        if (modelo != null) {
            try {
                // Usamos el método genérico del modelo
                List<Cliente> lista = modelo.getClientes();
                tablaClientes.setItems(FXCollections.observableArrayList(lista));
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al cargar clientes: " + e.getMessage());
            }
        }
    }

    @FXML
    public void cargarClientesEstandar() {
        if (modelo != null) {
            try {
                // Asumo que tienes este método en tu GestorDeDatos (lo vi en tu Controlador antiguo)
                List<Cliente> lista = modelo.getClientesEstandar();
                tablaClientes.setItems(FXCollections.observableArrayList(lista));
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al filtrar clientes estándar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void cargarClientesPremium() {
        if (modelo != null) {
            try {
                // Asumo que tienes este método en tu GestorDeDatos
                List<Cliente> lista = modelo.getClientesPremium();
                tablaClientes.setItems(FXCollections.observableArrayList(lista));
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al filtrar clientes premium: " + e.getMessage());
            }
        }
    }

    private void actualizarCombos() {
        try {
            // Llenamos el combo de Clientes con los Emails
            List<String> emails = modelo.getClientes().stream()
                    .map(c -> c.getEmail())
                    .collect(java.util.stream.Collectors.toList());
            comboPedCliente.setItems(FXCollections.observableArrayList(emails));

            // Llenamos el combo de Artículos con los Códigos
            List<String> codigos = modelo.getArticulos().stream()
                    .map(a -> a.getCodigo())
                    .collect(java.util.stream.Collectors.toList());
            comboPedArticulo.setItems(FXCollections.observableArrayList(codigos));
        } catch (Exception e) {
            System.err.println("Error actualizando combos: " + e.getMessage());
        }
    }

    @FXML
    public void accionAnadirPedido() {
        try {
            String emailCliente = comboPedCliente.getValue();
            String codigoArticulo = comboPedArticulo.getValue();

            if (emailCliente == null || codigoArticulo == null) {
                mostrarAlerta("Error", "Selecciona Cliente y Artículo de la lista.");
                return;
            }

            int unidades = Integer.parseInt(txtPedUnidades.getText());

            // Recuperamos los objetos completos desde la BBDD
            Cliente cliente = modelo.getCliente(emailCliente);
            Articulo articulo = modelo.getArticulo(codigoArticulo);

            // Creamos el pedido (Fecha actual)
            // IMPORTANTE: Ajusta el constructor a lo que tengas en tu clase Pedido
            // Asumo: Cliente, Articulo, Unidades, Fecha, Gastos, TiempoPrep
            java.time.LocalDateTime fecha = java.time.LocalDateTime.now();

            // Lógica de negocio (calculada aquí o en el constructor, según tu modelo)
            double gastosEnvio = articulo.getGastoEnvio();
            if (cliente instanceof ClientePremium) {
                gastosEnvio = gastosEnvio * 0.8; // 20% descuento [cite: 16]
            }

            Pedido p = new Pedido(cliente, articulo, unidades, fecha, gastosEnvio, articulo.getTiempoPreparacionMin()); // [cite: 21]

            modelo.crearPedido(p);

            mostrarAlerta("Éxito", "Pedido creado correctamente.");
            cargarPedidos(); // Refrescar tabla

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Las unidades deben ser un número entero.");
        } catch (Exception e) {
            mostrarAlerta("Error Crítico", "No se pudo crear el pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void accionEliminarPedido() {
        Pedido seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un pedido de la tabla para eliminar.");
            return;
        }

        try {
            // Llamamos al modelo que debe tener la lógica de comprobar la fecha vs tiempo preparación
            modelo.eliminarPedido(seleccionado.getNumeroPedido());
            mostrarAlerta("Éxito", "Pedido eliminado.");
            cargarPedidos();

        } catch (PedidoNoCancelableException e) { // [cite: 22]
            mostrarAlerta("No se puede eliminar", "El pedido ya no se puede cancelar (ha sido enviado).");
        } catch (Exception e) {
            // Si el modelo lanza string en vez de exception, capturamos genérico
            mostrarAlerta("Información", e.getMessage());
            cargarPedidos(); // Recargamos por si acaso
        }
    }

    @FXML
    public void cargarPedidos() {
        if (modelo == null) return;

        try {
            List<Pedido> resultados;
            String filtroEmail = txtFiltroEmail.getText();

            // Lógica de filtrado combinada (Pendiente/Enviado + Email)
            if (radioPendientes.isSelected()) {
                if (filtroEmail == null || filtroEmail.isEmpty()) {
                    resultados = modelo.getPedidosPendientes();
                } else {
                    resultados = modelo.getPedidosPendientes(filtroEmail);
                }
            } else {
                // Radio Enviados seleccionado
                if (filtroEmail == null || filtroEmail.isEmpty()) {
                    resultados = modelo.getPedidosEviados(); // Revisa si tu método es 'getPedidosEnviados' o 'Eviados' (typo común)
                } else {
                    resultados = modelo.getPedidosEviados(filtroEmail);
                }
            }

            tablaPedidos.setItems(FXCollections.observableArrayList(resultados));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
