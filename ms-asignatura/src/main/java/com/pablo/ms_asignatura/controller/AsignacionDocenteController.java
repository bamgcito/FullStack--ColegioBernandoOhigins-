package com.pablo.ms_asignatura.controller;

import com.pablo.ms_asignatura.dto.AsignacionDocenteDTO;
import com.pablo.ms_asignatura.dto.CrearAsignacionDTO;
import com.pablo.ms_asignatura.service.AsignacionDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/asignaciones")
public class AsignacionDocenteController {

    @Autowired
    private AsignacionDocenteService asignacionDocenteService;

    @PostMapping
    public ResponseEntity<Object> crearAsignacion(@RequestBody CrearAsignacionDTO solicitud) {
        return asignacionDocenteService.crearAsignacion(solicitud);
    }

    @GetMapping
    public List<AsignacionDocenteDTO> listarTodas() {
        return asignacionDocenteService.listarTodas();
    }

    @GetMapping("/curso/{cursoId}")
    public List<AsignacionDocenteDTO> buscarPorCurso(@PathVariable Long cursoId) {
        return asignacionDocenteService.buscarPorCurso(cursoId);
    }

    @GetMapping("/profesor/{profesorId}")
    public List<AsignacionDocenteDTO> buscarPorProfesor(@PathVariable Long profesorId) {
        return asignacionDocenteService.buscarPorProfesor(profesorId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        return asignacionDocenteService.buscarPorId(id);
    }
}
