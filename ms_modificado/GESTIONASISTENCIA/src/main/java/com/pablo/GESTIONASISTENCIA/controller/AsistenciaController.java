package com.pablo.GESTIONASISTENCIA.controller;

import com.pablo.GESTIONASISTENCIA.service.AsistenciaService;
import com.pablo.GESTIONASISTENCIA.dto.AsistenciaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping
    public String registrarAsistencia(@RequestBody AsistenciaDTO dto,
            @RequestHeader("Authorization") String auth) {
        return asistenciaService.registrarAsistencia(dto, auth);
    }

    @GetMapping("/alumno/{rut}")
    public Object obtenerAsistenciaPorAlumno(@PathVariable String rut,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        return asistenciaService.obtenerAsistenciaPorAlumno(rut, auth);
    }

    @GetMapping("/alumno/{rut}/porcentaje")
    public Object calcularPorcentaje(@PathVariable String rut,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        return asistenciaService.calcularPorcentaje(rut, auth);
    }

    @GetMapping("/asignacion/{asignacionId}")
    public Object obtenerAsistenciaPorAsignacion(@PathVariable Long asignacionId) {
        return asistenciaService.obtenerAsistenciaPorAsignacion(asignacionId);
    }
}