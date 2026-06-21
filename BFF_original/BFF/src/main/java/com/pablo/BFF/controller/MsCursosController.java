package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsCursosController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/cursos")
    public ResponseEntity<Object> crearCurso(@RequestBody Map<String, Object> body,
                                             @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsCursos(), "/cursos", body, token(auth));
    }

    @GetMapping("/cursos")
    public ResponseEntity<Object> listarCursos(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsCursos(), "/cursos", token(auth));
    }

    // Ruta específica antes que /{id} genérico
    @GetMapping("/cursos/profesor/{profesorId}")
    public ResponseEntity<Object> cursosPorProfesor(@PathVariable Long profesorId,
                                                    @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsCursos(), "/cursos/profesor/" + profesorId, token(auth));
    }

    @PostMapping("/cursos/{id}/alumnos")
    public ResponseEntity<Object> asignarAlumno(@PathVariable Long id,
                                                @RequestBody Map<String, Object> body,
                                                @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsCursos(), "/cursos/" + id + "/alumnos", body, token(auth));
    }

    @PostMapping("/cursos/{id}/profesor-jefe")
    public ResponseEntity<Object> asignarProfesorJefe(@PathVariable Long id,
                                                      @RequestBody Map<String, Object> body,
                                                      @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsCursos(), "/cursos/" + id + "/profesor-jefe", body, token(auth));
    }

    @GetMapping("/cursos/{id}/alumnos")
    public ResponseEntity<Object> alumnosDeCurso(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsCursos(), "/cursos/" + id + "/alumnos", token(auth));
    }

    @GetMapping("/cursos/alumno/{alumnoId}")
    public ResponseEntity<Object> cursoPorAlumno(@PathVariable Long alumnoId,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsCursos(), "/cursos/alumno/" + alumnoId, token(auth));
    }

    @GetMapping("/cursos/{id}")
    public ResponseEntity<Object> buscarCurso(@PathVariable Long id,
                                              @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsCursos(), "/cursos/" + id, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
