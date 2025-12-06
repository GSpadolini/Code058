package com.code058.model;

import javax.persistence.*;

@Entity
@Table(name = "articulo")
public class Articulo {
    @Id
    private String codigo;
    private String descripcion;
    @Column (name = "precio_venta")
    private Double precioVenta;//Se aconseja usar BigDecimal para precios en aplicaciones reales pero como ya usamos double, lo dejaremos asi, para no
    //cambiar el codigo. Porque eso da pie a que se nos escape algo.
    @Column (name = "gastos_envio")
    private Double gastoEnvio;
    @Column (name = "tiempo_preparacion_min")
    private Integer tiempoPreparacionMin;

    public Articulo() {}

    public Articulo(String codigo, String descripcion, double precioVenta, double gastoEnvio, int tiempoPreparacionMin) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.gastoEnvio = gastoEnvio;
        this.tiempoPreparacionMin = tiempoPreparacionMin;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getGastoEnvio() {
        return gastoEnvio;
    }

    public void setGastoEnvio(double gastoEnvio) {
        this.gastoEnvio = gastoEnvio;
    }

    public int getTiempoPreparacionMin() {
        return tiempoPreparacionMin;
    }

    public void setTiempoPreparacionMin(int tiempoPreparacionMin) {
        this.tiempoPreparacionMin = tiempoPreparacionMin;
    }

    @Override
    public String toString() {
        return "Articulo{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precioVenta=" + precioVenta +
                ", gastoEnvio=" + gastoEnvio +
                ", tiempoPreparacionMin=" + tiempoPreparacionMin +
                '}';
    }
}

