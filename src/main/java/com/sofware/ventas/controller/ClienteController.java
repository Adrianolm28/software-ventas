package com.sofware.ventas.controller;

import com.sofware.ventas.dto.ClienteDto;
import com.sofware.ventas.service.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {

    private final IClienteService clienteService;

    @GetMapping
    public ResponseEntity<Page<ClienteDto>> listarClientes(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(clienteService.listarClientes(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtenerClientePorId(@PathVariable String id) {
        ClienteDto dto = clienteService.obtenerClientePorId(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ClienteDto> guardarCliente(@RequestBody ClienteDto clienteDto) {
        ClienteDto clienteGuardado = clienteService.guardarCliente(clienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable String id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}