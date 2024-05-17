package com.devsu.account.dtos;


import lombok.Data;

import java.util.List;

@Data
public class CustomerDTO {
    private Long id;
    private String nombre;
    private String genero;
    private int edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private int customerId;
    private String contrasena;
    private boolean estado;
    private List<CustomerAccountDTO> accounts;
}
