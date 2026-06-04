package com.pablo.GESTIONASISTENCIA.service;

import com.pablo.GESTIONASISTENCIA.dto.AnotacionDTO;
import com.pablo.GESTIONASISTENCIA.dto.AsignacionDocenteDTO;
import com.pablo.GESTIONASISTENCIA.dto.UsuarioDTO;
import com.pablo.GESTIONASISTENCIA.model.Anotacion;
import com.pablo.GESTIONASISTENCIA.repository.AnotacionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnotacionService {

    @Autowired
    private AnotacionRepository anotacionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String registrarAnotacion(AnotacionDTO dto, String auth) {
        UsuarioDTO alumno = obtenerUsuarioPorRut(dto.getRutAlumno(), auth);
        if (alumno == null) {
            return "No existe un usuario con RUT: " + dto.getRutAlumno();
        }
        AsignacionDocenteDTO asignacion = obtenerAsignacion(dto.getAsignacionDocenteId());
        if (asignacion == null) {
            return "Asignación docente no encontrada con ID: " + dto.getAsignacionDocenteId();
        }
        String tipo = dto.getTipo();
        if (tipo == null || (!tipo.equals("POSITIVA") && !tipo.equals("NEGATIVA") && !tipo.equals("NEUTRA"))) {
            return "Tipo inválido. Use: POSITIVA, NEGATIVA o NEUTRA.";
        }
        anotacionRepository.save(Anotacion.desde(dto, alumno.getId()));
        return "Anotación registrada exitosamente.";
    }

    public Object obtenerAnotacionesPorAlumno(String rutAlumno, String auth) {
        UsuarioDTO alumno = obtenerUsuarioPorRut(rutAlumno, auth);
        if (alumno == null) {
            return "No existe un usuario con RUT: " + rutAlumno;
        }
        List<Anotacion> registros = anotacionRepository.findByAlumnoId(alumno.getId());
        List<AnotacionDTO> resultado = new ArrayList<>();
        for (Anotacion anotacion : registros) {
            resultado.add(AnotacionDTO.desde(anotacion));
        }
        return resultado;
    }

    public Object obtenerAnotacionesPorAsignacion(Long asignacionId) {
        List<Anotacion> registros = anotacionRepository.findByAsignacionDocenteId(asignacionId);
        List<AnotacionDTO> resultado = new ArrayList<>();
        for (Anotacion anotacion : registros) {
            resultado.add(AnotacionDTO.desde(anotacion));
        }
        return resultado;
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackUsuario")
    UsuarioDTO obtenerUsuario(Long id) {
        return restTemplate.getForObject("http://GESTIONUSUARIO/usuarios/" + id, UsuarioDTO.class);
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackUsuarioPorRut")
    UsuarioDTO obtenerUsuarioPorRut(String rut, String auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", auth);
        return restTemplate.exchange(
            "http://GESTIONUSUARIO/usuarios/rut/" + rut,
            HttpMethod.GET, new HttpEntity<>(headers), UsuarioDTO.class).getBody();
    }

    @CircuitBreaker(name = "academicoService", fallbackMethod = "fallbackAcademico")
    AsignacionDocenteDTO obtenerAsignacion(Long id) {
        return restTemplate.getForObject("http://GESTIONACADEMICO/asignaciones/" + id, AsignacionDocenteDTO.class);
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackAcceso")
    boolean verificarAccesoApoderado(Long apoderadoId, String rutAlumno) {
        Boolean resultado = restTemplate.getForObject(
            "http://GESTIONUSUARIO/usuarios/apoderados/" + apoderadoId + "/acceso?rutAlumno=" + rutAlumno,
            Boolean.class);
        return Boolean.TRUE.equals(resultado);
    }

    UsuarioDTO fallbackUsuario(Long id, Throwable t) { return null; }
    UsuarioDTO fallbackUsuarioPorRut(String rut, String auth, Throwable t) { return null; }
    AsignacionDocenteDTO fallbackAcademico(Long id, Throwable t) { return null; }
    boolean fallbackAcceso(Long id, String rut, Throwable t) { return false; }
}