package com.pablo.ms_asignatura.controller;

import com.pablo.ms_asignatura.dto.AsignaturaDTO;
import com.pablo.ms_asignatura.service.AsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/asignaturas")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @PostMapping
    public ResponseEntity<Object> crearAsignatura(@RequestBody AsignaturaDTO solicitud) {
        return asignaturaService.crearAsignatura(solicitud);
    }

    @GetMapping
    public List<AsignaturaDTO> listarTodas() {
        return asignaturaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        return asignaturaService.buscarPorId(id);
    }
}
