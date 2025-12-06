package com.code058.model;

import javax.persistence.*;

@Entity
@DiscriminatorValue("ESTANDAR")
public class ClienteEstandar extends Cliente{
    public ClienteEstandar() {
        super();
    }

    public ClienteEstandar(String email, String nombre, String domicilio, String nif) {
        super(email, nombre, domicilio, nif);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
