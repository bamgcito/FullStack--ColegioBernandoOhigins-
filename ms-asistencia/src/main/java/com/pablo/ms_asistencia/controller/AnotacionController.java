package com.pablo.ms_asistencia.controller;

import com.pablo.ms_asistencia.dto.AnotacionDTO;
import com.pablo.ms_asistencia.dto.CrearAnotacionDTO;
import com.pablo.ms_asistencia.service.AnotacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/anotaciones")
public class AnotacionController {

    @Autowired
    private AnotacionService anotacionService;

    @PostMapping
    public ResponseEntity<Object> registrarAnotacion(@RequestBody CrearAnotacionDTO solicitud) {
        return anotacionService.registrarAnotacion(solicitud);
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<AnotacionDTO> obtenerPorAlumno(@PathVariable Long alumnoId) {
        return anotacionService.obtenerPorAlumno(alumnoId);
    }

    @GetMapping("/asignacion/{asignacionId}")
    public List<AnotacionDTO> obtenerPorAsignacion(@PathVariable Long asignacionId) {
        return anotacionService.obtenerPorAsignacion(asignacionId);
    }
}
