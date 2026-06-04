package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/asistencia")
public class AsistenciaController {

    @Autowired
    private MicroservicioService ms;

    @GetMapping("/alumno/{rut}")
    public ResponseEntity<Object> asistenciaAlumno(@PathVariable String rut,
                                                    @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAsistencia(), "/asistencia/alumno/" + rut, token(auth));
    }

    @GetMapping("/alumno/{rut}/porcentaje")
    public ResponseEntity<Object> porcentajeAlumno(@PathVariable String rut,
                                                    @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAsistencia(), "/asistencia/alumno/" + rut + "/porcentaje", token(auth));
    }

    @GetMapping("/asignacion/{asignacionId}")
    public ResponseEntity<Object> asistenciaAsignacion(@PathVariable Long asignacionId,
                                                        @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAsistencia(), "/asistencia/asignacion/" + asignacionId, token(auth));
    }

    @PostMapping
    public ResponseEntity<Object> registrarAsistencia(@RequestBody Map<String, Object> body,
                                                       @RequestHeader("Authorization") String auth) {
        return ms.postForText(ms.urlAsistencia(), "/asistencia", body, token(auth));
    }

    @GetMapping("/anotaciones/alumno/{rut}")
    public ResponseEntity<Object> anotacionesAlumno(@PathVariable String rut,
                                                     @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAsistencia(), "/anotaciones/alumno/" + rut, token(auth));
    }

    @GetMapping("/anotaciones/asignacion/{asignacionId}")
    public ResponseEntity<Object> anotacionesAsignacion(@PathVariable Long asignacionId,
                                                         @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlAsistencia(), "/anotaciones/asignacion/" + asignacionId, token(auth));
    }

    @PostMapping("/anotaciones")
    public ResponseEntity<Object> registrarAnotacion(@RequestBody Map<String, Object> body,
                                                      @RequestHeader("Authorization") String auth) {
        return ms.postForText(ms.urlAsistencia(), "/anotaciones", body, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
