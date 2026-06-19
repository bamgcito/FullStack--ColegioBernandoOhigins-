package com.pablo.authservice.service;

import com.pablo.authservice.dto.LoginDTO;
import com.pablo.authservice.dto.UsuarioDTO;
import com.pablo.authservice.dto.ValidarTokenDTO;
import com.pablo.authservice.security.JwtUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackLogin")
    public ResponseEntity<Object> login(LoginDTO solicitud) {
        UsuarioDTO usuario = restTemplate.getForObject(
                "http://ms-usuarios/usuarios/rut/" + solicitud.getRut(), UsuarioDTO.class);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no encontrado con RUT: " + solicitud.getRut()));
        }

        if (!passwordEncoder.matches(solicitud.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta."));
        }

        String token = jwtUtil.generarToken(usuario.getRut(), usuario.getNombreRol(), usuario.getId());

        LoginDTO respuesta = new LoginDTO();
        respuesta.setId(usuario.getId());
        respuesta.setRut(usuario.getRut());
        respuesta.setNombreRol(usuario.getNombreRol());
        respuesta.setToken(token);

        return ResponseEntity.ok(respuesta);
    }

    private ResponseEntity<Object> fallbackLogin(LoginDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "El servicio de usuarios no está disponible. Intente nuevamente más tarde."));
    }

    public ValidarTokenDTO validarToken(String authHeader) {
        ValidarTokenDTO resultado = new ValidarTokenDTO();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resultado.setValido(false);
            return resultado;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.esValido(token)) {
            resultado.setValido(false);
            return resultado;
        }

        resultado.setValido(true);
        resultado.setRut(jwtUtil.extraerRut(token));
        resultado.setRol(jwtUtil.extraerRol(token));
        return resultado;
    }
}
