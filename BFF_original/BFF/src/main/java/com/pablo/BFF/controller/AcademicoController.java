package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/academico")
public class AcademicoController {

    @Autowired
    private MicroservicioService ms;

    @GetMapping("/asignaturas")
    public ResponseEntity<Object> listarAsignaturas(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAcademico(), "/asignaturas", token(auth));
    }

    @PostMapping("/asignaturas")
    public ResponseEntity<Object> crearAsignatura(@RequestBody Map<String, Object> body,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlAcademico(), "/asignaturas", body, token(auth));
    }

    @GetMapping("/asignaciones/curso/{cursoId}")
    public ResponseEntity<Object> listarAsignaciones(@PathVariable Long cursoId,
                                                      @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAcademico(), "/asignaciones/curso/" + cursoId, token(auth));
    }

    @GetMapping("/asignaciones/{id}")
    public ResponseEntity<Object> obtenerAsignacion(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAcademico(), "/asignaciones/" + id, token(auth));
    }

    @PostMapping("/asignaciones")
    public ResponseEntity<Object> crearAsignacion(@RequestBody Map<String, Object> body,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlAcademico(), "/asignaciones", body, token(auth));
    }

    @GetMapping("/evaluaciones/asignacion/{asignacionId}")
    public ResponseEntity<Object> listarEvaluaciones(@PathVariable Long asignacionId,
                                                      @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAcademico(), "/evaluaciones/asignacion/" + asignacionId, token(auth));
    }

    @PostMapping("/evaluaciones")
    public ResponseEntity<Object> crearEvaluacion(@RequestBody Map<String, Object> body,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlAcademico(), "/evaluaciones", body, token(auth));
    }

    @GetMapping("/notas/alumno")
    public ResponseEntity<Object> notasAlumno(@RequestParam String rutAlumno,
                                               @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAcademico(), "/notas/alumno?rutAlumno=" + rutAlumno, token(auth));
    }

    @GetMapping("/notas/alumno/promedio")
    public ResponseEntity<Object> promedioAlumno(@RequestParam String rutAlumno,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAcademico(), "/notas/alumno/promedio?rutAlumno=" + rutAlumno, token(auth));
    }

    @GetMapping("/notas/evaluacion/{evaluacionId}")
    public ResponseEntity<Object> notasEvaluacion(@PathVariable Long evaluacionId,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAcademico(), "/notas/evaluacion/" + evaluacionId, token(auth));
    }

    @PostMapping("/notas")
    public ResponseEntity<Object> registrarNota(@RequestBody Map<String, Object> body,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlAcademico(), "/notas", body, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
