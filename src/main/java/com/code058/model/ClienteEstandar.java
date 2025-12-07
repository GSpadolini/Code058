package com.code058.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import jakarta.persistence.Column;

@Entity
@DiscriminatorValue("ESTANDAR")
public class ClienteEstandar extends Cliente{
    public ClienteEstandar() {
    }

    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
