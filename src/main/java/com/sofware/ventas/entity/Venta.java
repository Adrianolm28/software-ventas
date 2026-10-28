package com.sofware.ventas.entity;

import com.sofware.ventas.enums.EstadoVenta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idVenta;

    private LocalDateTime fecha;
    private Double total;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoVenta estado = EstadoVenta.COMPLETADO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = true)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario vendedor;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();
}