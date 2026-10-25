package com.sofware.ventas.controller;

import com.sofware.ventas.dto.CategoriaDto;
import com.sofware.ventas.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaDto> guardarCategoria(@RequestBody CategoriaDto categoriaDto) {
        return ResponseEntity.ok(categoriaService.guardar(categoriaDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDto categoriaDto) {
        return ResponseEntity.ok(categoriaService.actualizar(id, categoriaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}