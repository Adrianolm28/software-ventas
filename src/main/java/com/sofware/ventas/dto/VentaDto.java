package com.sofware.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaDto {
    private String idVenta;
    private String idCliente;
    private String metodoPago;
    private String idVendedor;
    private LocalDateTime fecha;
    private Double total;
    private List<DetalleVentaDto> detalles;
}