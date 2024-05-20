package com.devsu.customer.entities;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Customer")
public class Customer extends Person {
    @Column(unique = true)
    private int customerId;

    private String contrasena;

    private boolean estado;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerAccount> accounts = new ArrayList<>();

    @Transient
    private List<?> transactions;

   @Builder
    public Customer(String nombre, Genero genero, int edad, String identificacion, String direccion, String telefono, int customerId, String contrasena, boolean estado, List<CustomerAccount> accounts, List<?> transactions) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.customerId = customerId;
        this.contrasena = contrasena;
        this.estado = estado;
        this.transactions = transactions;
        this.accounts = accounts;
    }

}
