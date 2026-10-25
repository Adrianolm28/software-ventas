package com.sofware.ventas.service;

import com.sofware.ventas.dto.ProductoDto;
import com.sofware.ventas.entity.Categoria;
import com.sofware.ventas.entity.Producto;
import com.sofware.ventas.mapper.Mapper;
import com.sofware.ventas.repository.CategoriaRepository;
import com.sofware.ventas.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final Mapper mapper;

    @Override
    public Page<ProductoDto> listarProductos(Pageable pageable) {
        return productoRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public ProductoDto obtenerProductoPorId(String id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        return producto != null ? mapper.toDto(producto) : null;
    }

    @Override
    public ProductoDto guardarProducto(ProductoDto productoDto) {
        Producto producto = new Producto();
        producto.setIdProducto(productoDto.getIdProducto());
        producto.setPrecio(productoDto.getPrecio());
        producto.setNombre(productoDto.getNombre());
        producto.setEstado(productoDto.getEstado() != null ? productoDto.getEstado() : true);

        // Buscar y asociar la categoría por su ID
        if (productoDto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(productoDto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + productoDto.getIdCategoria()));
            producto.setCategoria(categoria);
        }

        Producto productoGuardado = productoRepository.save(producto);
        return mapper.toDto(productoGuardado);
    }

    @Override
    public void desactivarProducto(String id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            producto.setEstado(false);
            productoRepository.save(producto);
        }
    }
}