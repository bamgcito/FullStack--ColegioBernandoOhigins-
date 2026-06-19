package com.pablo.ms_perfiles.controller;

import com.pablo.ms_perfiles.dto.ProfesorDTO;
import com.pablo.ms_perfiles.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/perfiles")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @PostMapping("/profesores")
    public ResponseEntity<Object> crearProfesor(@RequestBody ProfesorDTO solicitud) {
        return profesorService.crearProfesor(solicitud);
    }

    @GetMapping("/profesores/{usuarioId}")
    public ProfesorDTO buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return profesorService.buscarPorUsuarioId(usuarioId);
    }

    @GetMapping("/profesores/rut/{rut}")
    public ProfesorDTO buscarPorRut(@PathVariable String rut) {
        return profesorService.buscarPorRut(rut);
    }

    @GetMapping("/profesores")
    public List<ProfesorDTO> listarTodos() {
        return profesorService.listarTodos();
    }

    @DeleteMapping("/profesores/{usuarioId}")
    public ResponseEntity<Void> eliminarProfesor(@PathVariable Long usuarioId) {
        profesorService.eliminarPorUsuarioId(usuarioId);
        return ResponseEntity.ok().build();
    }
}
