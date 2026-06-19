package com.pablo.ms_perfiles.controller;

import com.pablo.ms_perfiles.dto.AlumnoDTO;
import com.pablo.ms_perfiles.dto.ApoderadoDTO;
import com.pablo.ms_perfiles.service.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/perfiles")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @PostMapping("/alumnos")
    public ResponseEntity<Object> crearAlumno(@RequestBody AlumnoDTO solicitud) {
        return alumnoService.crearAlumno(solicitud);
    }

    @GetMapping("/alumnos/{usuarioId}")
    public AlumnoDTO buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return alumnoService.buscarPorUsuarioId(usuarioId);
    }

    @GetMapping("/alumnos/rut/{rut}")
    public AlumnoDTO buscarPorRut(@PathVariable String rut) {
        return alumnoService.buscarPorRut(rut);
    }

    @GetMapping("/alumnos/{usuarioId}/apoderados")
    public List<ApoderadoDTO> listarApoderadosPorAlumno(@PathVariable Long usuarioId) {
        return alumnoService.listarApoderadosPorAlumno(usuarioId);
    }

    @GetMapping("/alumnos")
    public List<AlumnoDTO> listarTodos() {
        return alumnoService.listarTodos();
    }

    @DeleteMapping("/alumnos/{usuarioId}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Long usuarioId) {
        alumnoService.eliminarPorUsuarioId(usuarioId);
        return ResponseEntity.ok().build();
    }
}
