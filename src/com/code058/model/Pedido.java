package com.code058.model;

import java.time.LocalDateTime;

public class Pedido {
    private Cliente cliente;
    private Articulo articulo;

    private int numeroPedido;
    private int cantidad;
    private LocalDateTime fechaHora;
    private double gastoEnvio;
    private int tiempoPreparacion;


    public Pedido(){};

    public Pedido(Cliente cliente, Articulo articulo, int numeroPedido, int cantidad, LocalDateTime fechaHora, double gastoEnvio, int tiempoPreparacion) {
        this.cliente=cliente;
        this.articulo = articulo;
        this.numeroPedido = numeroPedido;
        this.cantidad = cantidad;
        this.fechaHora = fechaHora;
        this.gastoEnvio = gastoEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
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
                "numeroPedido=" + numeroPedido +
                ", cantidad=" + cantidad +
                ", fechaHora=" + fechaHora +
                ", gastoEnvio=" + gastoEnvio +
                ", tiempoPreparacion=" + tiempoPreparacion +
                '}';
    }
}
