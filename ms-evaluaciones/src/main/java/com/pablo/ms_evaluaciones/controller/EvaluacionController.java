package com.pablo.ms_evaluaciones.controller;

import com.pablo.ms_evaluaciones.dto.CrearEvaluacionDTO;
import com.pablo.ms_evaluaciones.dto.EvaluacionDTO;
import com.pablo.ms_evaluaciones.service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/evaluaciones")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<Object> crearEvaluacion(@RequestBody CrearEvaluacionDTO solicitud) {
        return evaluacionService.crearEvaluacion(solicitud);
    }

    @GetMapping
    public List<EvaluacionDTO> listarTodas() {
        return evaluacionService.listarTodas();
    }

    @GetMapping("/asignacion/{asignacionId}")
    public List<EvaluacionDTO> buscarPorAsignacion(@PathVariable Long asignacionId) {
        return evaluacionService.buscarPorAsignacion(asignacionId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        return evaluacionService.buscarPorId(id);
    }
}
