package com.pablo.GESTIONACADEMICO.controller;

import com.pablo.GESTIONACADEMICO.dto.EvaluacionDTO;
import com.pablo.GESTIONACADEMICO.service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/evaluaciones")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @PostMapping
    public String crearEvaluacion(@RequestBody EvaluacionDTO dto) {
        return evaluacionService.crearEvaluacion(dto);
    }

    @GetMapping("/asignacion/{asignacionId}")
    public List<EvaluacionDTO> listarPorAsignacion(@PathVariable Long asignacionId) {
        return evaluacionService.listarPorAsignacion(asignacionId);
    }
}
