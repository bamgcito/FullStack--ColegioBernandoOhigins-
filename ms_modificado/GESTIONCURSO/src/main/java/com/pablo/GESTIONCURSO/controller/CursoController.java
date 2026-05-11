package com.pablo.GESTIONCURSO.controller;

import com.pablo.GESTIONCURSO.dto.AsignarAlumnoDTO;
import com.pablo.GESTIONCURSO.dto.AsignarProfesorJefeDTO;
import com.pablo.GESTIONCURSO.dto.CursoDTO;
import com.pablo.GESTIONCURSO.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List<CursoDTO> listarCursos() {
        return cursoService.listarCursos();
    }

    @GetMapping("/{id}")
    public CursoDTO buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id);
    }

    @PostMapping("/alumnos")
    public String asignarAlumno(@RequestBody AsignarAlumnoDTO dto) {
        return cursoService.asignarAlumno(dto);
    }

    @PutMapping("/{id}/profesor-jefe")
    public String asignarProfesorJefe(@PathVariable Long id, @RequestBody AsignarProfesorJefeDTO dto) {
        return cursoService.asignarProfesorJefe(id, dto);
    }
}
