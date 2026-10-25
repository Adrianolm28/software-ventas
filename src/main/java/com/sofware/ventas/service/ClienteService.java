package com.sofware.ventas.service;

import com.sofware.ventas.dto.ClienteDto;
import com.sofware.ventas.entity.Cliente;
import com.sofware.ventas.mapper.Mapper;
import com.sofware.ventas.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;
    private final Mapper mapper;

    @Override
    public Page<ClienteDto> listarClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public ClienteDto obtenerClientePorId(String id) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        return cliente != null ? mapper.toDto(cliente) : null;
    }

    @Override
    public ClienteDto guardarCliente(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(clienteDto.getIdCliente());
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellidos(clienteDto.getApellidos());
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return mapper.toDto(clienteGuardado);
    }

    @Override
    public void eliminarCliente(String id) {
        clienteRepository.deleteById(id);
    }
}