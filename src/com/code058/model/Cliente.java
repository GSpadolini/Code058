package com.code058.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_cliente",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Cliente {
    private String nombre;
    private String domicilio;
    private String nif;
    @Id
    private String email;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    public Cliente (){};

    public Cliente(String email, String nombre, String domicilio, String nif) {
        this.email = email; // PK
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ClienteEstandar{" +
                "nombre='" + nombre + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", nif='" + nif + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public double tipoCuota(){return 0.0;}
    public double descuentoEnvio(){return 0.0;}
}
