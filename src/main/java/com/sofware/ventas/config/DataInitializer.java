package com.sofware.ventas.config;

import com.sofware.ventas.entity.Usuario;
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
        crearSiNoExiste("admin123", "admin123", "ADMIN");
        crearSiNoExiste("administrador", "admin123", "ADMINISTRADOR");
        crearSiNoExiste("vendedor", "vendedor123", "VENDEDOR");
    }

    private void crearSiNoExiste(String username, String password, String rol) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode(password));
            u.setRol(rol);
            usuarioRepository.save(u);
        }
    }
}
