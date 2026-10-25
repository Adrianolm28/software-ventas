package com.sofware.ventas.service;

import com.sofware.ventas.dto.DetalleVentaDto;
import com.sofware.ventas.dto.VentaDto;
import com.sofware.ventas.entity.DetalleVenta;
import com.sofware.ventas.entity.Producto;
import com.sofware.ventas.entity.Venta;
import com.sofware.ventas.mapper.Mapper;
import com.sofware.ventas.repository.ClienteRepository;
import com.sofware.ventas.repository.ProductoRepository;
import com.sofware.ventas.repository.UsuarioRepository;
import com.sofware.ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final Mapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<VentaDto> listarVentas(Pageable pageable) {
        return ventaRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDto obtenerVentaPorId(String id) {
        return ventaRepository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public VentaDto registrarVenta(VentaDto dto, String usernameVendedor) {
        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());

        // Guardar Método de Pago (por defecto EFECTIVO si llega nulo)
        venta.setMetodoPago(dto.getMetodoPago() != null ? dto.getMetodoPago() : "EFECTIVO");

        venta.setVendedor(usuarioRepository.findByUsername(usernameVendedor)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado")));

        // Manejo de Cliente Opcional (Cliente Frecuente)
        if (dto.getIdCliente() != null && !dto.getIdCliente().trim().isEmpty() && !"CLIENTE_FRECUENTE".equals(dto.getIdCliente())) {
            venta.setCliente(clienteRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
        } else {
            venta.setCliente(null); // Cliente Frecuente / Público General
        }

        double totalVenta = 0.0;

        for (DetalleVentaDto detDto : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detDto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(detDto.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            double subtotal = detDto.getCantidad() * producto.getPrecio();
            detalle.setSubtotal(subtotal);
            detalle.setVenta(venta);

            venta.getDetalles().add(detalle);
            totalVenta += subtotal;
        }

        venta.setTotal(totalVenta);

        Venta ventaGuardada = ventaRepository.save(venta);
        return mapper.toDto(ventaGuardada);
    }

    @Override
    @Transactional
    public void eliminarVenta(String id) {
        ventaRepository.deleteById(id);
    }
}