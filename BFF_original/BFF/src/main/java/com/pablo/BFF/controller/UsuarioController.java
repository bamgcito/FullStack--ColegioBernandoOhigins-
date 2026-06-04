package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/usuarios")
public class UsuarioController {

    @Autowired
    private MicroservicioService ms;

    @GetMapping
    public ResponseEntity<Object> listar(@RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlUsuarios(), "/usuarios", token(auth));
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody Map<String, Object> body,
                                        @RequestHeader("Authorization") String auth) {
        return ms.postForText(ms.urlUsuarios(), "/usuarios", body, token(auth));
    }

    @PostMapping("/alumnos")
    public ResponseEntity<Object> crearAlumno(@RequestBody Map<String, Object> body,
                                               @RequestHeader("Authorization") String auth) {
        return ms.postForText(ms.urlUsuarios(), "/usuarios/alumnos", body, token(auth));
    }

    @PostMapping("/profesores")
    public ResponseEntity<Object> crearProfesor(@RequestBody Map<String, Object> body,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.postForText(ms.urlUsuarios(), "/usuarios/profesores", body, token(auth));
    }

    @PostMapping("/apoderados")
    public ResponseEntity<Object> crearApoderado(@RequestBody Map<String, Object> body,
                                                  @RequestHeader("Authorization") String auth) {
        return ms.postForText(ms.urlUsuarios(), "/usuarios/apoderados", body, token(auth));
    }

    @PostMapping("/alumnos/apoderados")
    public ResponseEntity<Object> asociarApoderado(@RequestBody Map<String, Object> body,
                                                    @RequestHeader("Authorization") String auth) {
        return ms.postForText(ms.urlUsuarios(), "/usuarios/alumnos/apoderados", body, token(auth));
    }

    @GetMapping("/alumnos/{rut}/info")
    public ResponseEntity<Object> infoAlumno(@PathVariable String rut,
                                              @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlUsuarios(), "/usuarios/alumnos/" + rut + "/info", token(auth));
    }

    @GetMapping("/alumnos/id/{id}")
    public ResponseEntity<Object> alumnoPorId(@PathVariable Long id,
                                               @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlUsuarios(), "/usuarios/alumnos/id/" + id, token(auth));
    }

    @GetMapping("/profesores/{id}")
    public ResponseEntity<Object> profesorPorId(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlUsuarios(), "/usuarios/profesores/" + id, token(auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Long id,
                                            @RequestHeader("Authorization") String auth) {
        return ms.deleteForText(ms.urlUsuarios(), "/usuarios/" + id, token(auth));
    }

    @GetMapping("/apoderados/{id}/alumnos")
    public ResponseEntity<Object> alumnosDeApoderado(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlUsuarios(), "/usuarios/apoderados/" + id + "/alumnos", token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
