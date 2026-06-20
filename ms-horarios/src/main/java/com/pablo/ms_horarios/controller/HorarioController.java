package com.pablo.ms_horarios.controller;

import com.pablo.ms_horarios.dto.CrearHorarioDTO;
import com.pablo.ms_horarios.dto.HorarioDTO;
import com.pablo.ms_horarios.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @PostMapping
    public ResponseEntity<Object> crearHorario(@RequestBody CrearHorarioDTO solicitud) {
        return horarioService.crearHorario(solicitud);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarHorario(@PathVariable Long id) {
        return horarioService.eliminarHorario(id);
    }

    @GetMapping("/curso/{cursoId}")
    public List<HorarioDTO> obtenerPorCurso(@PathVariable Long cursoId) {
        return horarioService.obtenerPorCurso(cursoId);
    }

    @GetMapping("/profesor/{profesorId}")
    public List<HorarioDTO> obtenerPorProfesor(@PathVariable Long profesorId) {
        return horarioService.obtenerPorProfesor(profesorId);
    }

    @GetMapping("/asignacion/{asignacionId}")
    public List<HorarioDTO> obtenerPorAsignacion(@PathVariable Long asignacionId) {
        return horarioService.obtenerPorAsignacion(asignacionId);
    }
}
