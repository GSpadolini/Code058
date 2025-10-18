package com.code058.model;

public class ClientePremium extends Cliente{

    private double cuotaAnual;

    public ClientePremium() {}

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
        this.cuotaAnual = 30.00;
    }

    public double getCuotaAnual() {
        return cuotaAnual;
    }

    public void setCuotaAnual(double cuotaAnual) {
        this.cuotaAnual = cuotaAnual;
    }

    @Override
    public double descuentoEnvio(){
        return 0.2;
    }

    @Override
    public double tipoCuota(){return cuotaAnual;}

    @Override
    public String toString() {
        return "ClientePremium{" +
                "nombre='" + getNombre() + '\'' +
                ", domicilio='" + getDomicilio() + '\'' +
                ", nif='" + getNif() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", cuotaAnual=" + cuotaAnual +
                '}';
    }
}
