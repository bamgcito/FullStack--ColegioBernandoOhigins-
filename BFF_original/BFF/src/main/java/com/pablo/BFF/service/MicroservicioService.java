package com.pablo.BFF.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class MicroservicioService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("directRestTemplate")
    private RestTemplate directRestTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Object parseBody(String body) {
        if (body == null || body.isBlank()) return null;
        try {
            return objectMapper.readValue(body, Object.class);
        } catch (Exception e) {
            return Map.of("mensaje", body.trim());
        }
    }

    @Value("${ms.msauth}")
    private String urlMsAuth;

    @Value("${ms.msusuarios}")
    private String urlMsUsuarios;

    @Value("${ms.msperfiles}")
    private String urlMsPerfiles;

    @Value("${ms.mscursos}")
    private String urlMsCursos;

    @Value("${ms.msasignaturas}")
    private String urlMsAsignaturas;

    @Value("${ms.msevaluaciones}")
    private String urlMsEvaluaciones;

    @Value("${ms.msnotas}")
    private String urlMsNotas;

    @Value("${ms.msasistencia}")
    private String urlMsAsistencia;

    @Value("${ms.mscomunicacion}")
    private String urlMsComunicacion;

    @Value("${ms.mscertificados}")
    private String urlMsCertificados;

    @Value("${ms.mshorarios}")
    private String urlMsHorarios;

    // ── métodos con RestTemplate @LoadBalanced (servicios Eureka) ────────

    public ResponseEntity<Object> get(String baseUrl, String path, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange(baseUrl + path, HttpMethod.GET, entity, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(parseBody(resp.getBody()));
    }

    public ResponseEntity<Object> post(String baseUrl, String path, Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = restTemplate.exchange(baseUrl + path, HttpMethod.POST, entity, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(parseBody(resp.getBody()));
    }

    public ResponseEntity<Object> put(String baseUrl, String path, Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = restTemplate.exchange(baseUrl + path, HttpMethod.PUT, entity, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(parseBody(resp.getBody()));
    }

    public ResponseEntity<Object> delete(String baseUrl, String path, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange(baseUrl + path, HttpMethod.DELETE, entity, String.class);
        return ResponseEntity.status(resp.getStatusCode()).build();
    }

    // ── métodos con RestTemplate directo (sin Eureka — ms-comunicacion) ──

    public ResponseEntity<Object> directGet(String baseUrl, String path, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = directRestTemplate.exchange(baseUrl + path, HttpMethod.GET, entity, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(parseBody(resp.getBody()));
    }

    public ResponseEntity<Object> directPost(String baseUrl, String path, Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = directRestTemplate.exchange(baseUrl + path, HttpMethod.POST, entity, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(parseBody(resp.getBody()));
    }

    public ResponseEntity<Object> directPatch(String baseUrl, String path, Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = directRestTemplate.exchange(baseUrl + path, HttpMethod.PATCH, entity, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(parseBody(resp.getBody()));
    }

    // ── getters ──────────────────────────────────────────────────────────

    public String urlMsAuth()         { return urlMsAuth; }
    public String urlMsUsuarios()     { return urlMsUsuarios; }
    public String urlMsPerfiles()     { return urlMsPerfiles; }
    public String urlMsCursos()       { return urlMsCursos; }
    public String urlMsAsignaturas()  { return urlMsAsignaturas; }
    public String urlMsEvaluaciones() { return urlMsEvaluaciones; }
    public String urlMsNotas()        { return urlMsNotas; }
    public String urlMsAsistencia()   { return urlMsAsistencia; }
    public String urlMsComunicacion() { return urlMsComunicacion; }
    public String urlMsCertificados() { return urlMsCertificados; }
    public String urlMsHorarios()     { return urlMsHorarios; }
}
