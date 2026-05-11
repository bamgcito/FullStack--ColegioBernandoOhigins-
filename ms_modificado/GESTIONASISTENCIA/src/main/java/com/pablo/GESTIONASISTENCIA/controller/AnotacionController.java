package com.pablo.GESTIONASISTENCIA.controller;

import com.pablo.GESTIONASISTENCIA.dto.AnotacionDTO;
import com.pablo.GESTIONASISTENCIA.service.AnotacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anotaciones")
public class AnotacionController {

    @Autowired
    private AnotacionService anotacionService;

    @PostMapping
    public String registrarAnotacion(@RequestBody AnotacionDTO dto) {
        return anotacionService.registrarAnotacion(dto);
    }

    @GetMapping("/alumno/{rut}")
    public Object obtenerAnotacionesPorAlumno(@PathVariable String rut) {
        return anotacionService.obtenerAnotacionesPorAlumno(rut);
    }

    @GetMapping("/asignacion/{asignacionId}")
    public Object obtenerAnotacionesPorAsignacion(@PathVariable Long asignacionId) {
        return anotacionService.obtenerAnotacionesPorAsignacion(asignacionId);
    }
}
