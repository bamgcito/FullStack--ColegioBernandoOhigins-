package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/cursos")
public class CursoController {

    @Autowired
    private MicroservicioService ms;

    @GetMapping
    public ResponseEntity<Object> listar(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlCursos(), "/cursos", token(auth));
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody Map<String, Object> body,
                                        @RequestHeader("Authorization") String auth) {
        return ms.postForText(ms.urlCursos(), "/cursos", body, token(auth));
    }

    // Debe ir antes que /{id} para evitar que el Gateway y el BFF traten "profesor" como un ID
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<Object> cursosPorProfesor(@PathVariable Long profesorId,
                                                     @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlCursos(), "/cursos/profesor/" + profesorId, token(auth));
    }

    @PostMapping("/alumnos")
    public ResponseEntity<Object> asignarAlumno(@RequestBody Map<String, Object> body,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.postForText(ms.urlCursos(), "/cursos/alumnos", body, token(auth));
    }

    @PutMapping("/{id}/profesor-jefe")
    public ResponseEntity<Object> asignarProfesorJefe(@PathVariable Long id,
                                                       @RequestBody Map<String, Object> body,
                                                       @RequestHeader("Authorization") String auth) {
        return ms.putForText(ms.urlCursos(), "/cursos/" + id + "/profesor-jefe", body, token(auth));
    }

    @GetMapping("/{id}/alumnos")
    public ResponseEntity<Object> alumnosDeCurso(@PathVariable Long id,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlCursos(), "/cursos/" + id + "/alumnos", token(auth));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id,
                                               @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlCursos(), "/cursos/" + id, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}