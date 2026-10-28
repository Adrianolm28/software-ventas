package com.sofware.ventas.service;

import com.sofware.ventas.dto.DetalleVentaDto;
import com.sofware.ventas.dto.VentaDto;
import com.sofware.ventas.entity.DetalleVenta;
import com.sofware.ventas.entity.Producto;
import com.sofware.ventas.entity.Venta;
import com.sofware.ventas.enums.EstadoVenta;
import com.sofware.ventas.mapper.Mapper;
import com.sofware.ventas.repository.ClienteRepository;
import com.sofware.ventas.repository.ProductoRepository;
import com.sofware.ventas.repository.UsuarioRepository;
import com.sofware.ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La venta debe contener productos."
            );
        }

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setMetodoPago(dto.getMetodoPago() != null ? dto.getMetodoPago() : "EFECTIVO");
        venta.setEstado(EstadoVenta.COMPLETADO);

        venta.setVendedor(usuarioRepository.findByUsername(usernameVendedor)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vendedor no encontrado"
                )));

        if (dto.getIdCliente() != null
                && !dto.getIdCliente().trim().isEmpty()
                && !"CLIENTE_FRECUENTE".equals(dto.getIdCliente())) {
            venta.setCliente(clienteRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Cliente no encontrado"
                    )));
        }

        double totalVenta = 0.0;

        for (DetalleVentaDto detDto : dto.getDetalles()) {
            if (detDto.getIdProducto() == null
                    || detDto.getIdProducto().isBlank()
                    || detDto.getCantidad() == null
                    || detDto.getCantidad() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El producto y una cantidad mayor que cero son obligatorios."
                );
            }

            Producto producto = productoRepository.findById(detDto.getIdProducto())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Producto no encontrado: " + detDto.getIdProducto()
                    ));

            if (Boolean.FALSE.equals(producto.getEstado())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El producto está desactivado: " + producto.getNombre()
                );
            }

            int stockActual = producto.getStock() != null ? producto.getStock() : 0;

            if (stockActual < detDto.getCantidad()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para: " + producto.getNombre()
                );
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(detDto.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            double subtotal = detDto.getCantidad() * producto.getPrecio();
            detalle.setSubtotal(subtotal);
            detalle.setVenta(venta);

            producto.setStock(stockActual - detDto.getCantidad());
            productoRepository.save(producto);

            venta.getDetalles().add(detalle);
            totalVenta += subtotal;
        }

        venta.setTotal(totalVenta);

        Venta ventaGuardada = ventaRepository.save(venta);

        return mapper.toDto(ventaGuardada);
    }

    @Override
    @Transactional
    public void cancelarVenta(String id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Venta no encontrada."
                ));

        if (venta.getEstado() == EstadoVenta.CANCELADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La venta ya está cancelada."
            );
        }

        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                if (detalle.getProducto() == null
                        || detalle.getCantidad() == null
                        || detalle.getCantidad() <= 0) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "No se puede devolver el stock de esta venta."
                    );
                }

                Producto producto = detalle.getProducto();

                int stockActual = producto.getStock() != null
                        ? producto.getStock()
                        : 0;

                producto.setStock(stockActual + detalle.getCantidad());

                productoRepository.save(producto);
            }
        }

        venta.setEstado(EstadoVenta.CANCELADO);
        ventaRepository.save(venta);
    }
}