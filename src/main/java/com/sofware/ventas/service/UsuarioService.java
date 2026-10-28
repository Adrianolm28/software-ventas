package com.sofware.ventas.service;

import com.sofware.ventas.dto.UsuarioDto;
import com.sofware.ventas.entity.Usuario;
import com.sofware.ventas.enums.RolUsuario;
import com.sofware.ventas.mapper.Mapper;
import com.sofware.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        validarPassword(dto.getPassword());

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
            validarPassword(dto.getPassword());
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

    private void validarPassword(String password) {
        if (password == null ||
                !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9\\s]).{8,}$")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un carácter especial."
            );
        }
    }
}