package com.pablo.BFF.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MicroservicioService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ms.usuarios}")
    private String urlUsuarios;

    @Value("${ms.cursos}")
    private String urlCursos;

    @Value("${ms.academico}")
    private String urlAcademico;

    @Value("${ms.asistencia}")
    private String urlAsistencia;

    public ResponseEntity<Object> get(String baseUrl, String path, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(baseUrl + path, HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> post(String baseUrl, String path, Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(baseUrl + path, HttpMethod.POST, entity, Object.class);
    }

    public ResponseEntity<Object> put(String baseUrl, String path, Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(baseUrl + path, HttpMethod.PUT, entity, Object.class);
    }

    public String urlUsuarios() { return urlUsuarios; }
    public String urlCursos()   { return urlCursos; }
    public String urlAcademico(){ return urlAcademico; }
    public String urlAsistencia(){ return urlAsistencia; }
}
