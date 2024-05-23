package com.devsu.customer.dtos;
import java.util.List;

import com.devsu.customer.entities.Genero;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String nombre;
    private Genero genero;
    private int edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private boolean estado;
    private List<AccountResponseDTO> accounts;

}
