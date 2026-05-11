package com.pablo.GESTIONACADEMICO.controller;

import com.pablo.GESTIONACADEMICO.dto.AsignacionDocenteDTO;
import com.pablo.GESTIONACADEMICO.service.AsignacionDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/asignaciones")
public class AsignacionDocenteController {

    @Autowired
    private AsignacionDocenteService asignacionDocenteService;

    @PostMapping
    public String crearAsignacion(@RequestBody AsignacionDocenteDTO dto) {
        return asignacionDocenteService.crearAsignacion(dto);
    }

    @GetMapping("/curso/{cursoId}")
    public List<AsignacionDocenteDTO> listarPorCurso(@PathVariable Long cursoId) {
        return asignacionDocenteService.listarPorCurso(cursoId);
    }

    @GetMapping("/{id}")
    public AsignacionDocenteDTO buscarPorId(@PathVariable Long id) {
        return asignacionDocenteService.buscarPorId(id);
    }
}
