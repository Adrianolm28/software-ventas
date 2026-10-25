package com.sofware.ventas.controller;

import com.sofware.ventas.dto.LoginRequest;
import com.sofware.ventas.dto.LoginResponse;
import com.sofware.ventas.entity.Usuario;
import com.sofware.ventas.repository.UsuarioRepository;
import com.sofware.ventas.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println(">>> 1. Autenticando usuario...");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()));

            System.out.println(">>> 2. Buscando usuario en BD...");
            Usuario u = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();

            System.out.println(">>> 3. Generando token...");
            String rol = (u.getRol() != null) ? u.getRol().toUpperCase() : "ROLE_USER";
            String token = jwtService.generarToken(u.getUsername(), rol);

            System.out.println(">>> 4. Login exitoso, retornando respuesta...");
            return ResponseEntity.ok(new LoginResponse(token, u.getUsername(), rol));

        } catch (BadCredentialsException e) {
            System.out.println(">>> ERROR: Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Usuario o contraseña incorrectos"));
        } catch (Exception e) {
            System.out.println(">>> ERROR GRAVE EN LOGIN: " + e.getMessage());
            e.printStackTrace(); // Revisa esta traza en la consola de Spring
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error interno: " + e.getMessage()));
        }
    }
}
