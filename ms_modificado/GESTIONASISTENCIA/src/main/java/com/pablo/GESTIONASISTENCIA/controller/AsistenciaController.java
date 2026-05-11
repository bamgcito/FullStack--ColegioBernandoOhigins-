package com.pablo.GESTIONASISTENCIA.controller;

import com.pablo.GESTIONASISTENCIA.service.AsistenciaService;
import com.pablo.GESTIONASISTENCIA.dto.AsistenciaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping
    public String registrarAsistencia(@RequestBody AsistenciaDTO dto) {
        return asistenciaService.registrarAsistencia(dto);
    }

    @GetMapping("/alumno/{rut}")
    public Object obtenerAsistenciaPorAlumno(@PathVariable String rut) {
        return asistenciaService.obtenerAsistenciaPorAlumno(rut);
    }

    @GetMapping("/alumno/{rut}/porcentaje")
    public Object calcularPorcentaje(@PathVariable String rut) {
        return asistenciaService.calcularPorcentaje(rut);
    }

    @GetMapping("/asignacion/{asignacionId}")
    public Object obtenerAsistenciaPorAsignacion(@PathVariable Long asignacionId) {
        return asistenciaService.obtenerAsistenciaPorAsignacion(asignacionId);
    }
}
