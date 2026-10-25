package com.sofware.ventas.controller;

import com.sofware.ventas.dto.UsuarioDto;
import com.sofware.ventas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioDto>> listar(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarUsuarios(pageable));
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> crear(@RequestBody UsuarioDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizar(@PathVariable String id, @RequestBody UsuarioDto dto) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id, Authentication authentication) {
        String authority = authentication.getAuthorities().iterator().next().getAuthority();
        String rolSolicitante = authority.replace("ROLE_", "");

        usuarioService.eliminarUsuario(id, rolSolicitante);
        return ResponseEntity.noContent().build();
    }
}