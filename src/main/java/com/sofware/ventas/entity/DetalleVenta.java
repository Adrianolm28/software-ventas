package com.sofware.ventas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idDetalle;

    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta")
    private Venta venta;
}