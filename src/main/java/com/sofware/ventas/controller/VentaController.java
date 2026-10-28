package com.sofware.ventas.controller;

import com.sofware.ventas.dto.VentaDto;
import com.sofware.ventas.service.IVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VentaController {

    private final IVentaService ventaService;

    @GetMapping
    public ResponseEntity<Page<VentaDto>> listarVentas(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ventaService.listarVentas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDto> obtenerVentaPorId(@PathVariable String id) {
        VentaDto dto = ventaService.obtenerVentaPorId(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<VentaDto> registrarVenta(@RequestBody VentaDto ventaDto, Authentication authentication) {
        String usernameVendedor = authentication.getName();
        VentaDto ventaGuardada = ventaService.registrarVenta(ventaDto, usernameVendedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaGuardada);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarVenta(@PathVariable String id) {
        ventaService.cancelarVenta(id);
        return ResponseEntity.noContent().build();
    }
}