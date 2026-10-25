package com.sofware.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaDto {
    private String idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}