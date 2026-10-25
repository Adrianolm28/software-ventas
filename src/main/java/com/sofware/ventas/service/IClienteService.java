package com.sofware.ventas.service;

import com.sofware.ventas.dto.ClienteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClienteService {
    Page<ClienteDto> listarClientes(Pageable pageable);
    ClienteDto obtenerClientePorId(String id);
    ClienteDto guardarCliente(ClienteDto clienteDto);
    void eliminarCliente(String id);
}