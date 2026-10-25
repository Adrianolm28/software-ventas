package com.sofware.ventas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
@NoArgsConstructor @Getter
@AllArgsConstructor @Setter
public class Producto {

    @Id
    private String idProducto;

    private Double precio;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    private Boolean estado;
}