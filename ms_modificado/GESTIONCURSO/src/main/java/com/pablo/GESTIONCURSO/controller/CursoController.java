package com.pablo.GESTIONCURSO.controller;

import com.pablo.GESTIONCURSO.dto.AlumnoDetalleDTO;
import com.pablo.GESTIONCURSO.dto.AsignarAlumnoDTO;
import com.pablo.GESTIONCURSO.dto.AsignarProfesorJefeDTO;
import com.pablo.GESTIONCURSO.dto.CursoDTO;
import com.pablo.GESTIONCURSO.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public String crearCurso(@RequestBody CursoDTO dto) {
        return cursoService.crearCurso(dto);
    }

    @GetMapping
    public List<CursoDTO> listarCursos(
            @RequestHeader(value = "Authorization", required = false) String auth) {
        return cursoService.listarCursos(auth);
    }

    // Debe ir antes que /{id} para que Spring MVC no interprete "profesor" como un ID
    @GetMapping("/profesor/{profesorId}")
    public List<CursoDTO> cursosPorProfesor(@PathVariable Long profesorId) {
        return cursoService.obtenerCursosPorProfesor(profesorId);
    }

    @GetMapping("/{id}/alumnos")
    public List<AlumnoDetalleDTO> obtenerAlumnos(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        return cursoService.obtenerAlumnosDeCurso(id, auth);
    }

    @GetMapping("/{id}")
    public CursoDTO buscarPorId(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        return cursoService.buscarPorId(id, auth);
    }

    @PostMapping("/alumnos")
    public String asignarAlumno(@RequestBody AsignarAlumnoDTO dto,
            @RequestHeader("Authorization") String auth) {
        return cursoService.asignarAlumno(dto, auth);
    }

    @PutMapping("/{id}/profesor-jefe")
    public String asignarProfesorJefe(@PathVariable Long id,
            @RequestBody AsignarProfesorJefeDTO dto,
            @RequestHeader("Authorization") String auth) {
        return cursoService.asignarProfesorJefe(id, dto, auth);
    }
}