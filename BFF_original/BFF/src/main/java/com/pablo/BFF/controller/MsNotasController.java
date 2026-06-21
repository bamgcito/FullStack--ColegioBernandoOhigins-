package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsNotasController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/notas")
    public ResponseEntity<Object> registrarNota(@RequestBody Map<String, Object> body,
                                                @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsNotas(), "/notas", body, token(auth));
    }

    // Rutas específicas antes que /alumno/{alumnoId} genérico
    @GetMapping("/notas/alumno/{alumnoId}/promedio")
    public ResponseEntity<Object> promedioAlumno(@PathVariable Long alumnoId,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsNotas(), "/notas/alumno/" + alumnoId + "/promedio", token(auth));
    }

    @GetMapping("/notas/alumno/{alumnoId}/asignacion/{asignacionId}")
    public ResponseEntity<Object> notasPorAsignacion(@PathVariable Long alumnoId,
                                                     @PathVariable Long asignacionId,
                                                     @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsNotas(), "/notas/alumno/" + alumnoId + "/asignacion/" + asignacionId, token(auth));
    }

    @GetMapping("/notas/alumno/{alumnoId}")
    public ResponseEntity<Object> notasAlumno(@PathVariable Long alumnoId,
                                              @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsNotas(), "/notas/alumno/" + alumnoId, token(auth));
    }

    @GetMapping("/notas/evaluacion/{evaluacionId}")
    public ResponseEntity<Object> notasEvaluacion(@PathVariable Long evaluacionId,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsNotas(), "/notas/evaluacion/" + evaluacionId, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
