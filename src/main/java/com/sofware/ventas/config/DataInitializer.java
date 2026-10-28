package com.sofware.ventas.config;

import com.sofware.ventas.entity.Usuario;
import com.sofware.ventas.enums.RolUsuario;
import com.sofware.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        crearUsuario("admin123", "admin123", RolUsuario.ADMIN);
        crearUsuario("vendedor", "vendedor123", RolUsuario.VENDEDOR);
    }

    private void crearUsuario(String username, String password, RolUsuario rol) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(password));
        }

        usuario.setRol(rol);
        usuarioRepository.save(usuario);
    }
}