package com.pablo.ms_usuarios.controller;

import com.pablo.ms_usuarios.dto.UsuarioDTO;
import com.pablo.ms_usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Map<String, String>> crearUsuario(@RequestBody UsuarioDTO solicitud) {
        String message = usuarioService.crearUsuario(solicitud);
        return ResponseEntity.ok(Map.of("mensaje", message));
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/rut/{rut}")
    public UsuarioDTO buscarPorRut(@PathVariable String rut) {
        return usuarioService.buscarPorRut(rut);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        return usuarioService.eliminarUsuario(id);
    }

    @GetMapping
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.listarTodos();
    }
}
