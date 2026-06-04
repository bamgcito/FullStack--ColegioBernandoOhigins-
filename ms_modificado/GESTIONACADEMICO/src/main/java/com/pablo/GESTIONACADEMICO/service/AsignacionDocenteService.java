package com.pablo.GESTIONACADEMICO.service;

import com.pablo.GESTIONACADEMICO.dto.AsignacionDocenteDTO;
import com.pablo.GESTIONACADEMICO.dto.CursoDTO;
import com.pablo.GESTIONACADEMICO.dto.UsuarioDTO;
import com.pablo.GESTIONACADEMICO.model.Asignatura;
import com.pablo.GESTIONACADEMICO.model.AsignacionDocente;
import com.pablo.GESTIONACADEMICO.repository.AsignacionDocenteRepository;
import com.pablo.GESTIONACADEMICO.repository.AsignaturaRepository;
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
public class AsignacionDocenteService {

    @Autowired
    private AsignacionDocenteRepository asignacionDocenteRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackCrearAsignacion")
    public String crearAsignacion(AsignacionDocenteDTO dto, String auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", auth);
        UsuarioDTO profesor = restTemplate.exchange(
            "http://GESTIONUSUARIO/usuarios/rut/" + dto.getRutProfesor(),
            HttpMethod.GET, new HttpEntity<>(headers), UsuarioDTO.class).getBody();

        if (profesor == null) {
            return "No existe un usuario con RUT: " + dto.getRutProfesor();
        }

        Asignatura asignatura = asignaturaRepository.findById(dto.getAsignaturaId()).orElse(null);
        if (asignatura == null) {
            return "Asignatura no encontrada con ID: " + dto.getAsignaturaId();
        }
        if (asignacionDocenteRepository.existsByCursoIdAndAsignaturaId(dto.getCursoId(), dto.getAsignaturaId())) {
            return "Ya existe una asignación para ese curso y asignatura.";
        }

        asignacionDocenteRepository.save(AsignacionDocente.desde(dto, asignatura, profesor.getId()));
        return "Asignación docente creada exitosamente.";
    }

    public List<AsignacionDocenteDTO> listarPorCurso(Long cursoId) {
        List<AsignacionDocente> asignaciones = asignacionDocenteRepository.findByCursoId(cursoId);
        List<AsignacionDocenteDTO> resultado = new ArrayList<>();
        for (AsignacionDocente asignacion : asignaciones) {
            resultado.add(AsignacionDocenteDTO.desde(asignacion));
        }
        return resultado;
    }

    public AsignacionDocenteDTO buscarPorId(Long id) {
        AsignacionDocente asignacion = asignacionDocenteRepository.findById(id).orElse(null);
        if (asignacion == null)
            return null;
        return AsignacionDocenteDTO.desde(asignacion);
    }

    public UsuarioDTO obtenerUsuario(Long id) {
        try {
            return restTemplate.getForObject("http://GESTIONUSUARIO/usuarios/" + id, UsuarioDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public UsuarioDTO obtenerUsuarioPorRut(String rut) {
        try {
            return restTemplate.getForObject("http://GESTIONUSUARIO/usuarios/rut/" + rut, UsuarioDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public CursoDTO obtenerCurso(Long id) {
        try {
            return restTemplate.getForObject("http://GESTIONCURSO/cursos/" + id, CursoDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    private String fallbackCrearAsignacion(AsignacionDocenteDTO dto, String auth, Throwable t) {
        return "El servicio de usuarios no está disponible. Intente nuevamente más tarde.";
    }
}