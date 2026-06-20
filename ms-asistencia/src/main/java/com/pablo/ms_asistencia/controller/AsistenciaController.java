package com.pablo.ms_asistencia.controller;

import com.pablo.ms_asistencia.dto.AsistenciaDTO;
import com.pablo.ms_asistencia.dto.CrearAsistenciaDTO;
import com.pablo.ms_asistencia.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping
    public ResponseEntity<Object> registrarAsistencia(@RequestBody CrearAsistenciaDTO solicitud) {
        return asistenciaService.registrarAsistencia(solicitud);
    }

    @GetMapping("/alumno/{alumnoId}/porcentaje")
    public ResponseEntity<Object> calcularPorcentaje(@PathVariable Long alumnoId) {
        return asistenciaService.calcularPorcentaje(alumnoId);
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<AsistenciaDTO> obtenerPorAlumno(@PathVariable Long alumnoId) {
        return asistenciaService.obtenerPorAlumno(alumnoId);
    }

    @GetMapping("/asignacion/{asignacionId}")
    public List<AsistenciaDTO> obtenerPorAsignacion(@PathVariable Long asignacionId) {
        return asistenciaService.obtenerPorAsignacion(asignacionId);
    }
}
