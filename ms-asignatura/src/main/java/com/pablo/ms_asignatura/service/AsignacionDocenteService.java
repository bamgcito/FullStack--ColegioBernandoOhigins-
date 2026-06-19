package com.pablo.ms_asignatura.service;

import com.pablo.ms_asignatura.dto.*;
import com.pablo.ms_asignatura.model.Asignatura;
import com.pablo.ms_asignatura.model.AsignacionDocente;
import com.pablo.ms_asignatura.repository.AsignaturaRepository;
import com.pablo.ms_asignatura.repository.AsignacionDocenteRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AsignacionDocenteService {

    @Autowired
    private AsignacionDocenteRepository asignacionDocenteRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "serviciosExternos", fallbackMethod = "fallbackCrearAsignacion")
    public ResponseEntity<Object> crearAsignacion(CrearAsignacionDTO solicitud) {
        Asignatura asignatura = asignaturaRepository.findById(solicitud.getAsignaturaId()).orElse(null);
        if (asignatura == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe asignatura con ID: " + solicitud.getAsignaturaId()));
        }

        ProfesorPerfilDTO profesor = restTemplate.getForObject(
                "http://ms-perfiles/perfiles/profesores/rut/" + solicitud.getRutProfesor(), ProfesorPerfilDTO.class);

        if (profesor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe perfil de profesor con RUT: " + solicitud.getRutProfesor()));
        }

        Long profesorId = profesor.getUsuarioId();

        CursoExternoDTO curso = restTemplate.getForObject(
                "http://ms-cursos/cursos/" + solicitud.getCursoId(), CursoExternoDTO.class);

        if (curso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe curso con ID: " + solicitud.getCursoId()));
        }

        if (asignacionDocenteRepository.existsByAsignatura_IdAndProfesorIdAndCursoId(
                solicitud.getAsignaturaId(), profesorId, solicitud.getCursoId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe una asignacion para ese profesor, asignatura y curso."));
        }

        AsignacionDocente asignacion = new AsignacionDocente();
        asignacion.setAsignatura(asignatura);
        asignacion.setProfesorId(profesorId);
        asignacion.setCursoId(solicitud.getCursoId());
        asignacionDocenteRepository.save(asignacion);

        return ResponseEntity.ok(Map.of("mensaje", "Asignacion docente creada exitosamente."));
    }

    private ResponseEntity<Object> fallbackCrearAsignacion(CrearAsignacionDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Uno o más servicios externos no están disponibles. Intente nuevamente más tarde."));
    }

    public List<AsignacionDocenteDTO> listarTodas() {
        List<AsignacionDocente> asignaciones = asignacionDocenteRepository.findAll();
        List<AsignacionDocenteDTO> resultado = new ArrayList<>();
        for (AsignacionDocente a : asignaciones) {
            AsignacionDocenteDTO dto = AsignacionDocenteDTO.desde(a);
            dto.setNombreProfesor(obtenerNombreProfesor(a.getProfesorId()));
            dto.setNombreCurso(obtenerNombreCurso(a.getCursoId()));
            resultado.add(dto);
        }
        return resultado;
    }

    public ResponseEntity<Object> buscarPorId(Long id) {
        AsignacionDocente a = asignacionDocenteRepository.findById(id).orElse(null);
        if (a == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe asignacion con ID: " + id));
        }
        AsignacionDocenteDTO dto = AsignacionDocenteDTO.desde(a);
        dto.setNombreProfesor(obtenerNombreProfesor(a.getProfesorId()));
        dto.setNombreCurso(obtenerNombreCurso(a.getCursoId()));
        return ResponseEntity.ok(dto);
    }

    public List<AsignacionDocenteDTO> buscarPorCurso(Long cursoId) {
        List<AsignacionDocente> asignaciones = asignacionDocenteRepository.findByCursoId(cursoId);
        List<AsignacionDocenteDTO> resultado = new ArrayList<>();
        String nombreCurso = obtenerNombreCurso(cursoId);
        for (AsignacionDocente a : asignaciones) {
            AsignacionDocenteDTO dto = AsignacionDocenteDTO.desde(a);
            dto.setNombreProfesor(obtenerNombreProfesor(a.getProfesorId()));
            dto.setNombreCurso(nombreCurso);
            resultado.add(dto);
        }
        return resultado;
    }

    public List<AsignacionDocenteDTO> buscarPorProfesor(Long profesorId) {
        List<AsignacionDocente> asignaciones = asignacionDocenteRepository.findByProfesorId(profesorId);
        List<AsignacionDocenteDTO> resultado = new ArrayList<>();
        String nombreProfesor = obtenerNombreProfesor(profesorId);
        for (AsignacionDocente a : asignaciones) {
            AsignacionDocenteDTO dto = AsignacionDocenteDTO.desde(a);
            dto.setNombreProfesor(nombreProfesor);
            dto.setNombreCurso(obtenerNombreCurso(a.getCursoId()));
            resultado.add(dto);
        }
        return resultado;
    }

    private String obtenerNombreProfesor(Long profesorId) {
        try {
            ProfesorPerfilDTO profesor = restTemplate.getForObject(
                    "http://ms-perfiles/perfiles/profesores/" + profesorId, ProfesorPerfilDTO.class);
            if (profesor != null) {
                return profesor.getNombre() + " " + profesor.getApellido();
            }
        } catch (Exception e) {

        }
        return null;
    }

    private String obtenerNombreCurso(Long cursoId) {
        try {
            CursoExternoDTO curso = restTemplate.getForObject(
                    "http://ms-cursos/cursos/" + cursoId, CursoExternoDTO.class);
            if (curso != null) {
                return curso.getNivel() + curso.getLetra();
            }
        } catch (Exception e) {

        }
        return null;
    }
}
