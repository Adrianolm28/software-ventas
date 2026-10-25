package com.sofware.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDto {
    private Long idCategoria;
    private String nombre;
    private String descripcion;
}