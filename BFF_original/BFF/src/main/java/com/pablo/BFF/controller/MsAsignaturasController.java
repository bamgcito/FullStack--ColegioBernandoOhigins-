package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsAsignaturasController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/asignaturas")
    public ResponseEntity<Object> crearAsignatura(@RequestBody Map<String, Object> body,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsAsignaturas(), "/asignaturas", body, token(auth));
    }

    @GetMapping("/asignaturas")
    public ResponseEntity<Object> listarAsignaturas(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsignaturas(), "/asignaturas", token(auth));
    }

    @GetMapping("/asignaturas/{id}")
    public ResponseEntity<Object> buscarAsignatura(@PathVariable Long id,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsignaturas(), "/asignaturas/" + id, token(auth));
    }

    @PostMapping("/asignaciones")
    public ResponseEntity<Object> crearAsignacion(@RequestBody Map<String, Object> body,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsAsignaturas(), "/asignaciones", body, token(auth));
    }

    @GetMapping("/asignaciones")
    public ResponseEntity<Object> listarAsignaciones(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsignaturas(), "/asignaciones", token(auth));
    }

    // Rutas específicas antes que /{id} genérico
    @GetMapping("/asignaciones/curso/{cursoId}")
    public ResponseEntity<Object> asignacionesPorCurso(@PathVariable Long cursoId,
                                                       @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsignaturas(), "/asignaciones/curso/" + cursoId, token(auth));
    }

    @GetMapping("/asignaciones/profesor/{profesorId}")
    public ResponseEntity<Object> asignacionesPorProfesor(@PathVariable Long profesorId,
                                                          @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsignaturas(), "/asignaciones/profesor/" + profesorId, token(auth));
    }

    @GetMapping("/asignaciones/{id}")
    public ResponseEntity<Object> buscarAsignacion(@PathVariable Long id,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsignaturas(), "/asignaciones/" + id, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
