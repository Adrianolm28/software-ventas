package com.sofware.ventas.mapper;

import com.sofware.ventas.dto.*;
import com.sofware.ventas.entity.*;
import com.sofware.ventas.enums.EstadoVenta;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Component
public class Mapper {

    public ClienteDto toDto(Cliente cliente) {
        if (cliente == null) return null;
        ClienteDto dto = new ClienteDto();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombre(cliente.getNombre());
        dto.setApellidos(cliente.getApellidos());
        return dto;
    }

    public ProductoDto toDto(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoDto dto = new ProductoDto();

        dto.setIdProducto(producto.getIdProducto());
        dto.setPrecio(producto.getPrecio());
        dto.setNombre(producto.getNombre());
        dto.setMarca(producto.getMarca());
        dto.setStock(producto.getStock());
        dto.setEstado(producto.getEstado());

        if (producto.getCategoria() != null) {
            dto.setCategoria(producto.getCategoria().getNombre());
            dto.setIdCategoria(producto.getCategoria().getIdCategoria());
        }

        return dto;
    }
    public UsuarioDto toDto(Usuario usuario){
        if (usuario == null) return null;
        UsuarioDto dto = new UsuarioDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setUsername(usuario.getUsername());
        dto.setPassword(usuario.getPassword());
        dto.setRol(usuario.getRol().name());
        return dto;
    }

    public CategoriaDto toDto(Categoria categoria) {
        if (categoria == null) return null;
        CategoriaDto dto = new CategoriaDto();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }

    public DetalleVentaDto toDto(DetalleVenta detalle) {
        if (detalle == null) return null;
        DetalleVentaDto dto = new DetalleVentaDto();
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());

        if (detalle.getProducto() != null) {
            dto.setIdProducto(detalle.getProducto().getIdProducto());
            dto.setNombreProducto(detalle.getProducto().getNombre());
        }
        return dto;
    }

    public VentaDto toDto(Venta venta) {
        if (venta == null) return null;
        VentaDto dto = new VentaDto();
        dto.setIdVenta(venta.getIdVenta());
        dto.setFecha(venta.getFecha());
        dto.setTotal(venta.getTotal());
        dto.setMetodoPago(venta.getMetodoPago() != null ? venta.getMetodoPago() : "EFECTIVO");
        dto.setEstado(
                venta.getEstado() != null
                        ? venta.getEstado()
                        : EstadoVenta.COMPLETADO
        );

        if (venta.getCliente() != null) {
            dto.setIdCliente(venta.getCliente().getIdCliente());
        }
        if (venta.getVendedor() != null) {
            dto.setIdVendedor(venta.getVendedor().getIdUsuario());
        }
        if (venta.getDetalles() != null) {
            dto.setDetalles(venta.getDetalles().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setDetalles(new ArrayList<>());
        }
        return dto;
    }
}