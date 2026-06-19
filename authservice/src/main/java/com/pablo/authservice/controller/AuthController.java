package com.pablo.authservice.controller;

import com.pablo.authservice.dto.LoginDTO;
import com.pablo.authservice.dto.ValidarTokenDTO;
import com.pablo.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO solicitud) {
        return authService.login(solicitud);
    }

    @GetMapping("/validar")
    public ValidarTokenDTO validar(@RequestHeader("Authorization") String authHeader) {
        return authService.validarToken(authHeader);
    }
}
