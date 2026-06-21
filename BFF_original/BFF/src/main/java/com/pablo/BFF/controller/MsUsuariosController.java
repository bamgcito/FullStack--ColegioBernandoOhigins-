package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsUsuariosController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/usuarios")
    public ResponseEntity<Object> crearUsuario(@RequestBody Map<String, Object> body,
                                               @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsUsuarios(), "/usuarios", body, token(auth));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<Object> listarUsuarios(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsUsuarios(), "/usuarios", token(auth));
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Object> buscarUsuario(@PathVariable Long id,
                                                @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsUsuarios(), "/usuarios/" + id, token(auth));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Object> eliminarUsuario(@PathVariable Long id,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.delete(ms.urlMsUsuarios(), "/usuarios/" + id, token(auth));
    }

    @PostMapping("/roles")
    public ResponseEntity<Object> crearRol(@RequestBody Map<String, Object> body,
                                           @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsUsuarios(), "/roles", body, token(auth));
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> listarRoles(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsUsuarios(), "/roles", token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
