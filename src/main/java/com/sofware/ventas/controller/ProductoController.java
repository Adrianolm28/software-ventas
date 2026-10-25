package com.sofware.ventas.controller;

import com.sofware.ventas.dto.ProductoDto;
import com.sofware.ventas.service.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {

    private final IProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<ProductoDto>> listarProductos(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(productoService.listarProductos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> obtenerProductoPorId(@PathVariable String id) {
        ProductoDto dto = productoService.obtenerProductoPorId(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ProductoDto> guardarProducto(@RequestBody ProductoDto productoDto) {
        ProductoDto productoGuardado = productoService.guardarProducto(productoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarProducto(@PathVariable String id) {
        productoService.desactivarProducto(id);
        return ResponseEntity.noContent().build();
    }
}