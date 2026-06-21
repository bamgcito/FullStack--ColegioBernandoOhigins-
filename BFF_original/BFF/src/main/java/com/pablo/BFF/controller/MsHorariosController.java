package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsHorariosController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/horarios")
    public ResponseEntity<Object> crearHorario(@RequestBody Map<String, Object> body,
                                               @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsHorarios(), "/horarios", body, token(auth));
    }

    @DeleteMapping("/horarios/{id}")
    public ResponseEntity<Object> eliminarHorario(@PathVariable Long id,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.delete(ms.urlMsHorarios(), "/horarios/" + id, token(auth));
    }

    // Rutas específicas antes que /{id} genérico
    @GetMapping("/horarios/curso/{cursoId}")
    public ResponseEntity<Object> obtenerPorCurso(@PathVariable Long cursoId,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsHorarios(), "/horarios/curso/" + cursoId, token(auth));
    }

    @GetMapping("/horarios/profesor/{profesorId}")
    public ResponseEntity<Object> obtenerPorProfesor(@PathVariable Long profesorId,
                                                     @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsHorarios(), "/horarios/profesor/" + profesorId, token(auth));
    }

    @GetMapping("/horarios/asignacion/{asignacionId}")
    public ResponseEntity<Object> obtenerPorAsignacion(@PathVariable Long asignacionId,
                                                       @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsHorarios(), "/horarios/asignacion/" + asignacionId, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
