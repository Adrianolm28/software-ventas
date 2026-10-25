package com.sofware.ventas.service;

import com.sofware.ventas.dto.CategoriaDto;
import com.sofware.ventas.entity.Categoria;
import com.sofware.ventas.mapper.Mapper;
import com.sofware.ventas.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final Mapper mapper;

    public List<CategoriaDto> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoriaDto obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        return mapper.toDto(categoria);
    }

    public CategoriaDto guardar(CategoriaDto categoriaDto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDto.getNombre());
        categoria.setDescripcion(categoriaDto.getDescripcion());

        Categoria guardada = categoriaRepository.save(categoria);
        return mapper.toDto(guardada);
    }

    public CategoriaDto actualizar(Long id, CategoriaDto categoriaDto) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        existente.setNombre(categoriaDto.getNombre());
        existente.setDescripcion(categoriaDto.getDescripcion());

        Categoria actualizada = categoriaRepository.save(existente);
        return mapper.toDto(actualizada);
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}