package com.pablo.microservicio.controller;

import com.pablo.microservicio.dto.*;
import com.pablo.microservicio.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO solicitud) {
        return usuarioService.login(solicitud);
    }

    @PostMapping
    public String crearUsuario(@RequestBody UsuarioDTO solicitud) {
        return usuarioService.crearUsuario(solicitud);
    }

    @GetMapping
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/rut/{rut}")
    public UsuarioDTO buscarPorRut(@PathVariable String rut) {
        return usuarioService.buscarPorRut(rut);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        return usuarioService.eliminarUsuario(id);
    }

    @PostMapping("/alumnos")
    public String crearAlumno(@RequestBody AlumnoDTO solicitud) {
        return usuarioService.crearAlumno(solicitud);
    }

    @PostMapping("/profesores")
    public String crearProfesor(@RequestBody ProfesorDTO solicitud) {
        return usuarioService.crearProfesor(solicitud);
    }

    @PostMapping("/apoderados")
    public String crearApoderado(@RequestBody ApoderadoDTO solicitud) {
        return usuarioService.crearApoderado(solicitud);
    }

    @PostMapping("/alumnos/apoderados")
    public String asociarApoderado(@RequestBody AsociarApoderadoDTO solicitud) {
        return usuarioService.asociarApoderadoAAlumno(solicitud);
    }

    @GetMapping("/alumnos/{rut}/info")
    public Object obtenerInfoAlumno(@PathVariable String rut) {
        return usuarioService.obtenerInfoAlumno(rut);
    }

    @GetMapping("/profesores/{id}")
    public Object buscarProfesorPorId(@PathVariable Long id) {
        return usuarioService.buscarProfesorPorId(id);
    }

    @GetMapping("/apoderados/{apoderadoId}/acceso")
    public boolean verificarAccesoApoderado(@PathVariable Long apoderadoId, @RequestParam String rutAlumno) {
        return usuarioService.apoderadoTieneAccesoAAlumno(apoderadoId, rutAlumno);
    }

    @GetMapping("/alumnos/id/{id}")
    public AlumnoDTO buscarAlumnoPorId(@PathVariable Long id) {
        return usuarioService.buscarAlumnoPorId(id);
    }

    @GetMapping("/apoderados/{id}/alumnos")
    public ResponseEntity<Object> alumnosDeApoderado(@PathVariable Long id) {
        return usuarioService.obtenerAlumnosDeApoderado(id);
    }
}