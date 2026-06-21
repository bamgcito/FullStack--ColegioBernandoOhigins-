package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsAuthController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, Object> body) {
        return ms.post(ms.urlMsAuth(), "/auth/login", body, null);
    }
}
