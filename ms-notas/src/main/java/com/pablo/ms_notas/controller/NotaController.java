package com.pablo.ms_notas.controller;

import com.pablo.ms_notas.dto.CrearNotaDTO;
import com.pablo.ms_notas.dto.NotaDTO;
import com.pablo.ms_notas.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @PostMapping
    public ResponseEntity<Object> registrarNota(@RequestBody CrearNotaDTO solicitud) {
        return notaService.registrarNota(solicitud);
    }

    @GetMapping("/alumno/{alumnoId}/promedio")
    public ResponseEntity<Object> calcularPromedio(@PathVariable Long alumnoId) {
        return notaService.calcularPromedio(alumnoId);
    }

    @GetMapping("/alumno/{alumnoId}/asignacion/{asignacionId}")
    public List<NotaDTO> obtenerPorAlumnoYAsignacion(@PathVariable Long alumnoId, @PathVariable Long asignacionId) {
        return notaService.obtenerPorAlumnoYAsignacion(alumnoId, asignacionId);
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<NotaDTO> obtenerPorAlumno(@PathVariable Long alumnoId) {
        return notaService.obtenerPorAlumno(alumnoId);
    }

    @GetMapping("/evaluacion/{evaluacionId}")
    public List<NotaDTO> obtenerPorEvaluacion(@PathVariable Long evaluacionId) {
        return notaService.obtenerPorEvaluacion(evaluacionId);
    }
}
