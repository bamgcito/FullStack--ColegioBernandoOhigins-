package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bff/ms")
public class MsCertificadosController {

    @Autowired
    private MicroservicioService ms;

    @GetMapping("/certificados/alumno/{alumnoId}")
    public ResponseEntity<Object> certificadoAlumno(@PathVariable Long alumnoId,
                                                    @RequestHeader("Authorization") String auth) {
        return ms.get(ms.urlMsCertificados(), "/certificados/alumno/" + alumnoId, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
