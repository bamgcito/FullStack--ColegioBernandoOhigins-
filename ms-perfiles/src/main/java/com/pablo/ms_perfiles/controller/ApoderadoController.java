package com.pablo.ms_perfiles.controller;

import com.pablo.ms_perfiles.dto.AlumnoDTO;
import com.pablo.ms_perfiles.dto.ApoderadoDTO;
import com.pablo.ms_perfiles.dto.AsociarDTO;
import com.pablo.ms_perfiles.service.ApoderadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/perfiles")
public class ApoderadoController {

    @Autowired
    private ApoderadoService apoderadoService;

    @PostMapping("/apoderados")
    public ResponseEntity<Object> crearApoderado(@RequestBody ApoderadoDTO solicitud) {
        return apoderadoService.crearApoderado(solicitud);
    }

    @GetMapping("/apoderados/{usuarioId}")
    public ApoderadoDTO buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return apoderadoService.buscarPorUsuarioId(usuarioId);
    }

    @PostMapping("/asociar")
    public ResponseEntity<Object> asociar(@RequestBody AsociarDTO solicitud) {
        return apoderadoService.asociar(solicitud);
    }

    @GetMapping("/apoderados/{usuarioId}/alumnos")
    public List<AlumnoDTO> listarAlumnosPorApoderado(@PathVariable Long usuarioId) {
        return apoderadoService.listarAlumnosPorApoderado(usuarioId);
    }

    @DeleteMapping("/apoderados/{usuarioId}")
    public ResponseEntity<Void> eliminarApoderado(@PathVariable Long usuarioId) {
        apoderadoService.eliminarPorUsuarioId(usuarioId);
        return ResponseEntity.ok().build();
    }
}
