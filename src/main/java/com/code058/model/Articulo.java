package com.code058.model;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "articulo")
public class Articulo {

    @Id
    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio_venta", precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "gastos_envio", precision = 10, scale = 2)
    private BigDecimal gastoEnvio;

    @Column(name = "tiempo_preparacion_min")
    private int tiempoPreparacionMin;

    // constructores, getters, setters y toString
    public Articulo() {}

    public Articulo(String codigo, String descripcion, double precioVenta, double gastoEnvio, int tiempoPreparacionMin) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioVenta = BigDecimal.valueOf(precioVenta).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.gastoEnvio = BigDecimal.valueOf(gastoEnvio).setScale(2, BigDecimal.ROUND_HALF_UP);
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

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public BigDecimal getGastoEnvio() { return gastoEnvio; }
    public void setGastoEnvio(BigDecimal gastoEnvio) { this.gastoEnvio = gastoEnvio; }

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

