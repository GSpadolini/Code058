package com.code058.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@DiscriminatorValue("PREMIUM")
public class ClientePremium extends Cliente {

    @Column(name = "cuota_anual", precision = 10, scale = 2)
    private BigDecimal cuotaAnual;

    @Column(name = "descuento_envio", precision = 5, scale = 2)
    private BigDecimal descuentoEnvio = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    public ClientePremium() {}

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
        this.cuotaAnual = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.descuentoEnvio = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getCuotaAnual() {
        return cuotaAnual;
    }

    public void setCuotaAnual(BigDecimal cuotaAnual) {
        this.cuotaAnual = (cuotaAnual != null)
                ? cuotaAnual.setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }

    public BigDecimal getDescuentoEnvio() {
        return descuentoEnvio;
    }

    public void setDescuentoEnvio(BigDecimal descuentoEnvio) {
        this.descuentoEnvio = (descuentoEnvio != null)
                ? descuentoEnvio.setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }

    @Override
    public double tipoCuota() {
        return (cuotaAnual != null) ? cuotaAnual.doubleValue() : 0.0;
    }

    @Override
    public double descuentoEnvio() {
        return (descuentoEnvio != null) ? descuentoEnvio.doubleValue() : 0.0;
    }

    @Override
    public String toString() {
        return "ClientePremium{" +
                "nombre='" + getNombre() + '\'' +
                ", domicilio='" + getDomicilio() + '\'' +
                ", nif='" + getNif() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", cuotaAnual=" + cuotaAnual +
                ", descuentoEnvio=" + descuentoEnvio +
                '}';
    }
}
