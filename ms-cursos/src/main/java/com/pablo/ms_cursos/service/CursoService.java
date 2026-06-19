package com.pablo.ms_cursos.service;

import com.pablo.ms_cursos.dto.*;
import com.pablo.ms_cursos.model.Curso;
import com.pablo.ms_cursos.model.CursoAlumno;
import com.pablo.ms_cursos.repository.CursoAlumnoRepository;
import com.pablo.ms_cursos.repository.CursoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CursoAlumnoRepository cursoAlumnoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<Object> crearCurso(CursoDTO solicitud) {
        Curso curso = new Curso();
        String nombre = solicitud.getNivel() + " " + solicitud.getLetra();
        curso.setNombre(nombre);
        curso.setNivel(solicitud.getNivel());
        curso.setLetra(solicitud.getLetra());
        curso.setAnio(solicitud.getAnio());
        cursoRepository.save(curso);
        return ResponseEntity.ok(Map.of("mensaje", "Curso creado exitosamente: " + nombre));
    }

    public List<CursoDTO> listarTodos() {
        List<Curso> cursos = cursoRepository.findAll();
        List<CursoDTO> resultado = new ArrayList<>();
        for (Curso c : cursos) {
            CursoDTO dto = CursoDTO.desde(c);
            if (c.getProfesorJefeId() != null) {
                dto.setNombreProfesorJefe(obtenerNombreProfesor(c.getProfesorJefeId()));
            }
            dto.setAlumnosCount((int) cursoAlumnoRepository.countByCurso(c));
            resultado.add(dto);
        }
        return resultado;
    }

    public CursoDTO buscarPorId(Long id) {
        Curso c = cursoRepository.findById(id).orElse(null);
        if (c == null) return null;
        CursoDTO dto = CursoDTO.desde(c);
        if (c.getProfesorJefeId() != null) {
            dto.setNombreProfesorJefe(obtenerNombreProfesor(c.getProfesorJefeId()));
        }
        return dto;
    }

    @CircuitBreaker(name = "perfilService", fallbackMethod = "fallbackAsignarAlumno")
    public ResponseEntity<Object> asignarAlumno(Long cursoId, AsignarAlumnoDTO solicitud) {
        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (curso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe curso con ID: " + cursoId));
        }

        AlumnoPerfilDTO alumno = restTemplate.getForObject(
                "http://ms-perfiles/perfiles/alumnos/rut/" + solicitud.getAlumnoRut(), AlumnoPerfilDTO.class);

        if (alumno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe perfil de alumno con RUT: " + solicitud.getAlumnoRut()));
        }

        if (cursoAlumnoRepository.existsByCursoAndAlumnoId(curso, alumno.getUsuarioId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El alumno ya está asignado a este curso."));
        }

        CursoAlumno cursoAlumno = new CursoAlumno();
        cursoAlumno.setCurso(curso);
        cursoAlumno.setAlumnoId(alumno.getUsuarioId());
        cursoAlumnoRepository.save(cursoAlumno);

        return ResponseEntity.ok(Map.of("mensaje", "Alumno asignado al curso exitosamente."));
    }

    private ResponseEntity<Object> fallbackAsignarAlumno(Long cursoId, AsignarAlumnoDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "El servicio de perfiles no está disponible. Intente nuevamente más tarde."));
    }

    @CircuitBreaker(name = "perfilService", fallbackMethod = "fallbackAsignarProfesorJefe")
    public ResponseEntity<Object> asignarProfesorJefe(Long cursoId, AsignarProfesorDTO solicitud) {
        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (curso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe curso con ID: " + cursoId));
        }

        ProfesorPerfilDTO profesor = restTemplate.getForObject(
                "http://ms-perfiles/perfiles/profesores/rut/" + solicitud.getProfesorJefeRut(), ProfesorPerfilDTO.class);

        if (profesor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe perfil de profesor con RUT: " + solicitud.getProfesorJefeRut()));
        }

        curso.setProfesorJefeId(profesor.getUsuarioId());
        cursoRepository.save(curso);

        return ResponseEntity.ok(Map.of("mensaje", "Profesor jefe asignado al curso exitosamente."));
    }

    private ResponseEntity<Object> fallbackAsignarProfesorJefe(Long cursoId, AsignarProfesorDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "El servicio de perfiles no está disponible. Intente nuevamente más tarde."));
    }

    public List<CursoAlumnoDTO> obtenerAlumnos(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (curso == null) return new ArrayList<>();
        List<CursoAlumno> asignaciones = cursoAlumnoRepository.findByCurso(curso);
        List<CursoAlumnoDTO> resultado = new ArrayList<>();
        for (CursoAlumno ca : asignaciones) {
            CursoAlumnoDTO dto = CursoAlumnoDTO.desde(ca);
            try {
                AlumnoPerfilDTO perfil = restTemplate.getForObject(
                        "http://ms-perfiles/perfiles/alumnos/" + ca.getAlumnoId(), AlumnoPerfilDTO.class);
                if (perfil != null) {
                    dto.setNombre(perfil.getNombre());
                    dto.setApellido(perfil.getApellido());
                    dto.setRut(perfil.getRut());
                }
            } catch (Exception ignored) {}
            resultado.add(dto);
        }
        return resultado;
    }

    public CursoDTO obtenerCursoPorAlumno(Long alumnoId) {
        Optional<CursoAlumno> ca = cursoAlumnoRepository.findByAlumnoId(alumnoId);
        if (ca.isEmpty()) return null;
        Curso c = ca.get().getCurso();
        CursoDTO dto = CursoDTO.desde(c);
        if (c.getProfesorJefeId() != null) {
            dto.setNombreProfesorJefe(obtenerNombreProfesor(c.getProfesorJefeId()));
        }
        return dto;
    }

    public List<CursoDTO> obtenerCursosPorProfesor(Long profesorId) {
        List<Curso> cursos = cursoRepository.findByProfesorJefeId(profesorId);
        List<CursoDTO> resultado = new ArrayList<>();
        for (Curso c : cursos) {
            CursoDTO dto = CursoDTO.desde(c);
            dto.setNombreProfesorJefe(obtenerNombreProfesor(profesorId));
            resultado.add(dto);
        }
        return resultado;
    }

    private String obtenerNombreProfesor(Long profesorJefeId) {
        try {
            ProfesorPerfilDTO profesor = restTemplate.getForObject(
                    "http://ms-perfiles/perfiles/profesores/" + profesorJefeId, ProfesorPerfilDTO.class);
            if (profesor != null) {
                return profesor.getNombre() + " " + profesor.getApellido();
            }
        } catch (Exception e) {

        }
        return null;
    }
}
