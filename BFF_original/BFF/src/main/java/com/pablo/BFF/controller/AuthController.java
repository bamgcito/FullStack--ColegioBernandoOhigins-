package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/auth")
public class AuthController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> body) {
        return ms.post(ms.urlUsuarios(), "/usuarios/login", body, null);
    }
}
