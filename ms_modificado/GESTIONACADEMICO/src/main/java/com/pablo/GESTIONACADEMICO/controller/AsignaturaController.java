package com.pablo.GESTIONACADEMICO.controller;

import com.pablo.GESTIONACADEMICO.dto.AsignaturaDTO;
import com.pablo.GESTIONACADEMICO.service.AsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/asignaturas")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @PostMapping
    public String crearAsignatura(@RequestBody AsignaturaDTO dto) {
        return asignaturaService.crearAsignatura(dto);
    }

    @GetMapping
    public List<AsignaturaDTO> listarAsignaturas() {
        return asignaturaService.listarAsignaturas();
    }

    @GetMapping("/{id}/detalle")
    public Object obtenerDetalle(@PathVariable Long id, @RequestParam Long cursoId) {
        return asignaturaService.obtenerDetalle(id, cursoId);
    }
}
