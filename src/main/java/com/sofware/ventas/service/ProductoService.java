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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final Mapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDto> listarProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDto obtenerProductoPorId(String id) {
        Producto producto = productoRepository.findById(id)
                .orElse(null);

        return producto != null ? mapper.toDto(producto) : null;
    }

    @Override
    @Transactional
    public ProductoDto guardarProducto(ProductoDto productoDto) {

        if (productoDto.getIdProducto() == null
                || productoDto.getIdProducto().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El código del producto es obligatorio."
            );
        }

        if (productoDto.getStock() != null
                && productoDto.getStock() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El stock no puede ser negativo."
            );
        }

        String idProducto = productoDto.getIdProducto();
        Producto producto = productoRepository.findById(idProducto)
                .orElseGet(Producto::new);

        boolean esNuevo = producto.getIdProducto() == null;

        if (esNuevo) {
            producto.setIdProducto(idProducto);
            producto.setEstado(true);
            producto.setStock(0);
        }
        if (productoDto.getPrecio() != null) {
            producto.setPrecio(productoDto.getPrecio());
        }

        if (productoDto.getNombre() != null) {
            producto.setNombre(productoDto.getNombre());
        }
        if (productoDto.getMarca() != null) {
            String marca = productoDto.getMarca().trim();

            producto.setMarca(marca.isEmpty() ? null : marca);
        }
        if (productoDto.getStock() != null) {
            producto.setStock(productoDto.getStock());
        } else if (producto.getStock() == null) {
            producto.setStock(0);
        }

        if (productoDto.getEstado() != null) {
            producto.setEstado(productoDto.getEstado());
        }

        if (productoDto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository
                    .findById(productoDto.getIdCategoria())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Categoría no encontrada con ID: "
                                    + productoDto.getIdCategoria()
                    ));

            producto.setCategoria(categoria);
        }

        Producto productoGuardado = productoRepository.save(producto);

        return mapper.toDto(productoGuardado);
    }

    @Override
    @Transactional
    public void desactivarProducto(String id) {
        Producto producto = productoRepository.findById(id)
                .orElse(null);

        if (producto != null) {
            producto.setEstado(false);
            productoRepository.save(producto);
        }
    }
}