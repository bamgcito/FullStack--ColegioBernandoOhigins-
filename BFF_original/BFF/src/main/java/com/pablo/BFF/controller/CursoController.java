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
        return ms.post(ms.urlCursos(), "/cursos", body, token(auth));
    }

    @PostMapping("/alumnos")
    public ResponseEntity<Object> asignarAlumno(@RequestBody Map<String, Object> body,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlCursos(), "/cursos/alumnos", body, token(auth));
    }

    @PutMapping("/{id}/profesor-jefe")
    public ResponseEntity<Object> asignarProfesorJefe(@PathVariable Long id,
                                                       @RequestBody Map<String, Object> body,
                                                       @RequestHeader("Authorization") String auth) {
        return ms.put(ms.urlCursos(), "/cursos/" + id + "/profesor-jefe", body, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
