package com.code058.model;

public class ClienteEstandar extends Cliente{
    public ClienteEstandar() {
    }

    public ClienteEstandar(String domicilio, String nombre, String nif, String email) {
        super(domicilio, nombre, nif, email);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
