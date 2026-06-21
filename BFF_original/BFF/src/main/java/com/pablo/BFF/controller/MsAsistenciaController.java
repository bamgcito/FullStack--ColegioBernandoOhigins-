package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsAsistenciaController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/asistencia")
    public ResponseEntity<Object> registrarAsistencia(@RequestBody Map<String, Object> body,
                                                      @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsAsistencia(), "/asistencia", body, token(auth));
    }

    // Ruta específica antes que /alumno/{alumnoId} genérico
    @GetMapping("/asistencia/alumno/{alumnoId}/porcentaje")
    public ResponseEntity<Object> porcentajeAlumno(@PathVariable Long alumnoId,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsistencia(), "/asistencia/alumno/" + alumnoId + "/porcentaje", token(auth));
    }

    @GetMapping("/asistencia/alumno/{alumnoId}")
    public ResponseEntity<Object> asistenciaAlumno(@PathVariable Long alumnoId,
                                                   @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsistencia(), "/asistencia/alumno/" + alumnoId, token(auth));
    }

    @GetMapping("/asistencia/asignacion/{asignacionId}")
    public ResponseEntity<Object> asistenciaAsignacion(@PathVariable Long asignacionId,
                                                       @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsistencia(), "/asistencia/asignacion/" + asignacionId, token(auth));
    }

    @PostMapping("/anotaciones")
    public ResponseEntity<Object> registrarAnotacion(@RequestBody Map<String, Object> body,
                                                     @RequestHeader("Authorization") String auth) {
        return ms.post(ms.urlMsAsistencia(), "/anotaciones", body, token(auth));
    }

    @GetMapping("/anotaciones/alumno/{alumnoId}")
    public ResponseEntity<Object> anotacionesAlumno(@PathVariable Long alumnoId,
                                                    @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsistencia(), "/anotaciones/alumno/" + alumnoId, token(auth));
    }

    @GetMapping("/anotaciones/asignacion/{asignacionId}")
    public ResponseEntity<Object> anotacionesAsignacion(@PathVariable Long asignacionId,
                                                        @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsAsistencia(), "/anotaciones/asignacion/" + asignacionId, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
