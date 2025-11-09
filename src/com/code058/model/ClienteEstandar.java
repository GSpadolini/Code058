package com.code058.model;

public class ClienteEstandar extends Cliente{
    public ClienteEstandar() {
    }

    public ClienteEstandar(String email, String nombre, String domicilio, String nif) {
        super(email, nombre, domicilio, nif);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
