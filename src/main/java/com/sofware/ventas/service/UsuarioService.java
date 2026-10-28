package com.sofware.ventas.service;

import com.sofware.ventas.dto.UsuarioDto;
import com.sofware.ventas.entity.Usuario;
import com.sofware.ventas.enums.RolUsuario;
import com.sofware.ventas.mapper.Mapper;
import com.sofware.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UsuarioDto> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public UsuarioDto crearUsuario(UsuarioDto dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(RolUsuario.VENDEDOR);

        return mapper.toDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDto actualizarUsuario(String id, UsuarioDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setUsername(dto.getUsername());
        usuario.setRol(RolUsuario.VENDEDOR);

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return mapper.toDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminarUsuario(String idUsuarioAEliminar, String rolSolicitante) {
        Usuario usuarioTarget = usuarioRepository.findById(idUsuarioAEliminar)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioRepository.delete(usuarioTarget);
    }
}