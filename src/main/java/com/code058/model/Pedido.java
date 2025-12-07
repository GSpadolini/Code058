package com.code058.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import jakarta.persistence.*;

@Entity
@Table(name = "pedido")
public class Pedido {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_email")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_codigo")
    private Articulo articulo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_pedido")
    private int numeroPedido;

    @Column(name = "unidades")
    private int cantidad;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaPedido;

    /**
     * Estos valores NO existen en la tabla `pedido` según tu esquema.
     * Los marcamos como transient para que Hibernate NO los valide como columnas.
     * Se calculan/obtienen a partir del Articulo asociado cuando haga falta.
     */
    @Transient
    private BigDecimal gastoEnvio; // calculado a partir de articulo.getGastoEnvio()

    @Transient
    private Integer tiempoPreparacion; // calculado a partir de articulo.getTiempoPreparacionMin()

    public Pedido() {}

    public Pedido(Cliente cliente, Articulo articulo, int numeroPedido, int cantidad,
                  LocalDateTime fechaPedido, BigDecimal gastoEnvio, int tiempoPreparacion) {
        this.cliente = cliente;
        this.articulo = articulo;
        this.numeroPedido = numeroPedido;
        this.cantidad = cantidad;
        this.fechaPedido = fechaPedido;
        // guardamos localmente pero no se persiste en la tabla pedido
        this.gastoEnvio = (gastoEnvio != null) ? gastoEnvio.setScale(2, RoundingMode.HALF_UP) : null;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public Pedido(Cliente cliente, Articulo articulo, int numeroPedido, int cantidad,
                  LocalDateTime fechaPedido, double gastoEnvioDouble, int tiempoPreparacion) {
        this(cliente, articulo, numeroPedido, cantidad, fechaPedido,
                BigDecimal.valueOf(gastoEnvioDouble).setScale(2, RoundingMode.HALF_UP),
                tiempoPreparacion);
    }

    // Getters / setters
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }

    public int getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(int numeroPedido) { this.numeroPedido = numeroPedido; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    /** GastoEnvio calculado: si no hay valor local, lo obtenemos del Articulo asociado */
    public BigDecimal getGastoEnvio() {
        if (gastoEnvio != null) return gastoEnvio;
        if (articulo != null && articulo.getGastoEnvio() != null) {
            return articulo.getGastoEnvio().setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
    public void setGastoEnvio(BigDecimal gastoEnvio) {
        this.gastoEnvio = (gastoEnvio != null) ? gastoEnvio.setScale(2, RoundingMode.HALF_UP) : null;
    }

    /** Conveniencia: obtener gastoEnvio como double si alguna parte del código lo necesita */
    public double getGastoEnvioAsDouble() {
        BigDecimal g = getGastoEnvio();
        return (g != null) ? g.doubleValue() : 0.0;
    }

    /** Tiempo de preparación: si no hay valor local, lo obtenemos de Articulo.tiempoPreparacionMin */
    public int getTiempoPreparacion() {
        if (tiempoPreparacion != null) return tiempoPreparacion;
        if (articulo != null) return articulo.getTiempoPreparacionMin();
        return 0;
    }
    public void setTiempoPreparacion(int tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }

    @Override
    public String toString() {
        return "Pedido{" +
                cliente +
                articulo + "\n" +
                "numeroPedido=" + numeroPedido +
                ", cantidad=" + cantidad +
                ", fechaHora=" + fechaPedido +
                ", gastoEnvio=" + (getGastoEnvio() != null ? getGastoEnvio().toPlainString() : "null") +
                ", tiempoPreparacion=" + getTiempoPreparacion() +
                '}';
    }

    public BigDecimal getPrecioTotal() {
        BigDecimal cantidadBD = BigDecimal.valueOf(this.cantidad);
        BigDecimal precioVenta = (articulo != null && articulo.getPrecioVenta() != null) ? articulo.getPrecioVenta() : BigDecimal.ZERO;
        BigDecimal subtotal = precioVenta.multiply(cantidadBD);

        BigDecimal descuento = BigDecimal.valueOf(1.0 - (cliente != null ? cliente.descuentoEnvio() : 0.0));
        BigDecimal gastoFinal = (getGastoEnvio() != null ? getGastoEnvio() : BigDecimal.ZERO)
                .multiply(descuento);

        BigDecimal total = subtotal.add(gastoFinal);
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean esCancelable() {
        LocalDateTime ahora = LocalDateTime.now();
        long minutosTranscurridos = ChronoUnit.MINUTES.between(fechaPedido, ahora);
        int prep = getTiempoPreparacion();
        return minutosTranscurridos < prep;
    }
}
