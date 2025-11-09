package com.code058.model;

public class ClientePremium extends Cliente{

    private double cuotaAnual;

    public ClientePremium(String email, String nombre, String domicilio, String nif, double cuotaAnual) {
        super(email, nombre, domicilio, nif); // LLAMA AL CONSTRUCTOR DE LA BASE
        this.cuotaAnual = cuotaAnual;
    }

    public ClientePremium(String email, String nombre, String domicilio, String nif) {
        super(email, nombre, domicilio, nif);
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
