package com.pablo.ms_cursos.controller;

import com.pablo.ms_cursos.dto.*;
import com.pablo.ms_cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity<Object> crearCurso(@RequestBody CursoDTO solicitud) {
        return cursoService.crearCurso(solicitud);
    }

    @GetMapping
    public List<CursoDTO> listarTodos() {
        return cursoService.listarTodos();
    }

    @GetMapping("/profesor/{profesorId}")
    public List<CursoDTO> obtenerCursosPorProfesor(@PathVariable Long profesorId) {
        return cursoService.obtenerCursosPorProfesor(profesorId);
    }

    @GetMapping("/{id}")
    public CursoDTO buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id);
    }

    @PostMapping("/{id}/alumnos")
    public ResponseEntity<Object> asignarAlumno(@PathVariable Long id, @RequestBody AsignarAlumnoDTO solicitud) {
        return cursoService.asignarAlumno(id, solicitud);
    }

    @PostMapping("/{id}/profesor-jefe")
    public ResponseEntity<Object> asignarProfesorJefe(@PathVariable Long id, @RequestBody AsignarProfesorDTO solicitud) {
        return cursoService.asignarProfesorJefe(id, solicitud);
    }

    @GetMapping("/{id}/alumnos")
    public List<CursoAlumnoDTO> obtenerAlumnos(@PathVariable Long id) {
        return cursoService.obtenerAlumnos(id);
    }

    @GetMapping("/alumno/{alumnoId}")
    public CursoDTO obtenerCursoPorAlumno(@PathVariable Long alumnoId) {
        return cursoService.obtenerCursoPorAlumno(alumnoId);
    }
}
