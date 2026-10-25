package com.sofware.ventas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cliente")
@NoArgsConstructor @Getter
@AllArgsConstructor @Setter
public class Cliente {

    @Id
    private String idCliente;

    private String nombre;

    private String apellidos;
}