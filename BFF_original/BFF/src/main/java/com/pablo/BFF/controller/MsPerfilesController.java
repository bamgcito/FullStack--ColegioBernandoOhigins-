package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsPerfilesController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/perfiles/alumnos")
    public ResponseEntity<Object> crearAlumno(@RequestBody Map<String, Object> body,
                                              @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsPerfiles(), "/perfiles/alumnos", body, token(auth));
    }

    @GetMapping("/perfiles/alumnos/{usuarioId}")
    public ResponseEntity<Object> buscarAlumno(@PathVariable Long usuarioId,
                                               @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsPerfiles(), "/perfiles/alumnos/" + usuarioId, token(auth));
    }

    @GetMapping("/perfiles/alumnos")
    public ResponseEntity<Object> listarAlumnos(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsPerfiles(), "/perfiles/alumnos", token(auth));
    }

    @GetMapping("/perfiles/alumnos/{usuarioId}/apoderados")
    public ResponseEntity<Object> apoderadosDeAlumno(@PathVariable Long usuarioId,
                                                     @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsPerfiles(), "/perfiles/alumnos/" + usuarioId + "/apoderados", token(auth));
    }

    @PostMapping("/perfiles/profesores")
    public ResponseEntity<Object> crearProfesor(@RequestBody Map<String, Object> body,
                                                @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsPerfiles(), "/perfiles/profesores", body, token(auth));
    }

    @GetMapping("/perfiles/profesores/{usuarioId}")
    public ResponseEntity<Object> buscarProfesor(@PathVariable Long usuarioId,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsPerfiles(), "/perfiles/profesores/" + usuarioId, token(auth));
    }

    @GetMapping("/perfiles/profesores")
    public ResponseEntity<Object> listarProfesores(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsPerfiles(), "/perfiles/profesores", token(auth));
    }

    @PostMapping("/perfiles/apoderados")
    public ResponseEntity<Object> crearApoderado(@RequestBody Map<String, Object> body,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsPerfiles(), "/perfiles/apoderados", body, token(auth));
    }

    // Ruta específica antes que /{usuarioId} genérico
    @GetMapping("/perfiles/apoderados/{usuarioId}/alumnos")
    public ResponseEntity<Object> alumnosDeApoderado(@PathVariable Long usuarioId,
                                                     @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsPerfiles(), "/perfiles/apoderados/" + usuarioId + "/alumnos", token(auth));
    }

    @GetMapping("/perfiles/apoderados/{usuarioId}")
    public ResponseEntity<Object> buscarApoderado(@PathVariable Long usuarioId,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsPerfiles(), "/perfiles/apoderados/" + usuarioId, token(auth));
    }

    @PostMapping("/perfiles/asociar")
    public ResponseEntity<Object> asociarApoderado(@RequestBody Map<String, Object> body,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsPerfiles(), "/perfiles/asociar", body, token(auth));
    }

    @DeleteMapping("/perfiles/alumnos/{usuarioId}")
    public ResponseEntity<Object> eliminarAlumno(@PathVariable Long usuarioId,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.delete(ms.urlMsPerfiles(), "/perfiles/alumnos/" + usuarioId, token(auth));
    }

    @DeleteMapping("/perfiles/profesores/{usuarioId}")
    public ResponseEntity<Object> eliminarProfesor(@PathVariable Long usuarioId,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.delete(ms.urlMsPerfiles(), "/perfiles/profesores/" + usuarioId, token(auth));
    }

    @DeleteMapping("/perfiles/apoderados/{usuarioId}")
    public ResponseEntity<Object> eliminarApoderado(@PathVariable Long usuarioId,
                                                    @RequestHeader("Authorization") String auth) {
        return ms.delete(ms.urlMsPerfiles(), "/perfiles/apoderados/" + usuarioId, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
