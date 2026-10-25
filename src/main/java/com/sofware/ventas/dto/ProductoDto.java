package com.sofware.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {
    private String idProducto;
    private Double precio;
    private String nombre;
    private String categoria;
    private Long idCategoria;
    private Boolean estado;
}