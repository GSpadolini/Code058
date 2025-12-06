package com.code058.model;

import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.*;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_pedido")
    private Integer numeroPedido;
    @ManyToOne
    @JoinColumn(name = "cliente_email")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "articulo_codigo")
    private Articulo articulo;
    @Column(name = "unidades")
    private Integer cantidad;
    @Column(name = "fecha_hora")
    private LocalDateTime fechaPedido;
    @Column(name = "gasto_envio")
    private Double gastoEnvio;
    @Column(name = "tiempo_preparacion_min")
    private Integer tiempoPreparacion;


    public Pedido(){};

    public Pedido(Cliente cliente, Articulo articulo, Integer cantidad, LocalDateTime fechaPedido, Double gastoEnvio, Integer tiempoPreparacion) {
        this.cliente=cliente;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.fechaPedido = fechaPedido;
        this.gastoEnvio = gastoEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public Pedido(Cliente cliente, Articulo articulo, Integer numeroPedido, Integer cantidad, LocalDateTime fechaPedido, Double gastoEnvio, Integer tiempoPreparacion) {
        this(cliente, articulo, cantidad, fechaPedido, gastoEnvio, tiempoPreparacion);
        this.numeroPedido = numeroPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

      public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public double getGastoEnvio() {
        return gastoEnvio;
    }

    public void setGastoEnvio(double gastoEnvio) {
        this.gastoEnvio = gastoEnvio;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                cliente +
                articulo + "\n" +
                "numeroPedido=" + numeroPedido +
                ", cantidad=" + cantidad +
                ", fechaHora=" + fechaPedido +
                ", gastoEnvio=" + gastoEnvio +
                ", tiempoPreparacion=" + tiempoPreparacion +
                '}';
    }

    public double getPrecioTotal(){
        double total;
        total = (articulo.getPrecioVenta()*cantidad) + (articulo.getGastoEnvio()*(1-cliente.descuentoEnvio()));
        return total;
    }


    public boolean esCancelable(){
        boolean cancelado = false;
        LocalDateTime ahora = LocalDateTime.now();
        long minutosTranscurridos = ChronoUnit.MINUTES.between(fechaPedido, ahora);
        if ((minutosTranscurridos) < tiempoPreparacion){
            cancelado = true;
        }
        return cancelado;
    }


}
