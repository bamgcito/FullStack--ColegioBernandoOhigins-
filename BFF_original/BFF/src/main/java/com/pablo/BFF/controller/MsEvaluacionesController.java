package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsEvaluacionesController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/evaluaciones")
    public ResponseEntity<Object> crearEvaluacion(@RequestBody Map<String, Object> body,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsEvaluaciones(), "/evaluaciones", body, token(auth));
    }

    @GetMapping("/evaluaciones")
    public ResponseEntity<Object> listarEvaluaciones(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsEvaluaciones(), "/evaluaciones", token(auth));
    }

    // Ruta específica antes que /{id} genérico
    @GetMapping("/evaluaciones/asignacion/{asignacionId}")
    public ResponseEntity<Object> evaluacionesPorAsignacion(@PathVariable Long asignacionId,
                                                            @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsEvaluaciones(), "/evaluaciones/asignacion/" + asignacionId, token(auth));
    }

    @GetMapping("/evaluaciones/{id}")
    public ResponseEntity<Object> buscarEvaluacion(@PathVariable Long id,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsEvaluaciones(), "/evaluaciones/" + id, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
