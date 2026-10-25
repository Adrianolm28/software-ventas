package com.sofware.ventas.service;

import com.sofware.ventas.dto.ProductoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductoService {
    Page<ProductoDto> listarProductos(Pageable pageable);
    ProductoDto obtenerProductoPorId(String id);
    ProductoDto guardarProducto(ProductoDto productoDto);
    void desactivarProducto(String id);
}