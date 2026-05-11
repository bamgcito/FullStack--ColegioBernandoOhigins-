package com.pablo.GESTIONACADEMICO.controller;

import com.pablo.GESTIONACADEMICO.dto.NotaDTO;
import com.pablo.GESTIONACADEMICO.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @PostMapping
    public String registrarNota(@RequestBody NotaDTO dto) {
        return notaService.registrarNota(dto);
    }

    @GetMapping("/alumno")
    public Object listarNotasAlumno(@RequestParam String rutAlumno) {
        return notaService.listarNotasAlumno(rutAlumno);
    }

    @GetMapping("/alumno/promedio")
    public Object calcularPromedio(@RequestParam String rutAlumno) {
        return notaService.calcularPromedioAlumno(rutAlumno);
    }

    @GetMapping("/evaluacion/{evaluacionId}")
    public List<NotaDTO> listarPorEvaluacion(@PathVariable Long evaluacionId) {
        return notaService.listarPorEvaluacion(evaluacionId);
    }
}
