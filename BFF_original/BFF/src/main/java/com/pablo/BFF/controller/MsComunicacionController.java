package com.pablo.BFF.controller;

import com.pablo.BFF.service.MicroservicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bff/ms")
public class MsComunicacionController {

    @Autowired
    private MicroservicioService ms;

    @PostMapping("/comunicacion/conversaciones")
    public ResponseEntity<Object> crearConversacion(@RequestBody Map<String, Object> body,
                                                    @RequestHeader("Authorization") String auth) {
        return ms.directPost(ms.urlMsComunicacion(), "/comunicacion/conversaciones", body, token(auth));
    }

    @GetMapping("/comunicacion/conversaciones/usuario/{usuarioId}")
    public ResponseEntity<Object> conversacionesUsuario(@PathVariable Long usuarioId,
                                                        @RequestHeader("Authorization") String auth) {
        return ms.directGet(ms.urlMsComunicacion(), "/comunicacion/conversaciones/usuario/" + usuarioId, token(auth));
    }

    @PostMapping("/comunicacion/mensajes")
    public ResponseEntity<Object> enviarMensaje(@RequestBody Map<String, Object> body,
                                                @RequestHeader("Authorization") String auth) {
        return ms.directPost(ms.urlMsComunicacion(), "/comunicacion/mensajes", body, token(auth));
    }

    @GetMapping("/comunicacion/mensajes/conversacion/{conversacionId}")
    public ResponseEntity<Object> mensajesConversacion(@PathVariable Long conversacionId,
                                                       @RequestHeader("Authorization") String auth) {
        return ms.directGet(ms.urlMsComunicacion(), "/comunicacion/mensajes/conversacion/" + conversacionId, token(auth));
    }

    @PostMapping("/comunicacion/notificaciones")
    public ResponseEntity<Object> crearNotificacion(@RequestBody Map<String, Object> body,
                                                    @RequestHeader("Authorization") String auth) {
        return ms.directPost(ms.urlMsComunicacion(), "/comunicacion/notificaciones", body, token(auth));
    }

    @GetMapping("/comunicacion/notificaciones/usuario/{usuarioId}")
    public ResponseEntity<Object> notificacionesUsuario(@PathVariable Long usuarioId,
                                                        @RequestHeader("Authorization") String auth) {
        return ms.directGet(ms.urlMsComunicacion(), "/comunicacion/notificaciones/usuario/" + usuarioId, token(auth));
    }

    @PatchMapping("/comunicacion/notificaciones/{id}/leer")
    public ResponseEntity<Object> marcarLeida(@PathVariable Long id,
                                              @RequestHeader("Authorization") String auth) {
        return ms.directPatch(ms.urlMsComunicacion(), "/comunicacion/notificaciones/" + id + "/leer", null, token(auth));
    }

    private String token(String auth) { return auth.replace("Bearer ", ""); }
}
