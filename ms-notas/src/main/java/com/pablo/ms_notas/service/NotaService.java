package com.pablo.ms_notas.service;

import com.pablo.ms_notas.dto.AlumnoPerfilDTO;
import com.pablo.ms_notas.dto.AsignacionExternaDTO;
import com.pablo.ms_notas.dto.CrearNotaDTO;
import com.pablo.ms_notas.dto.EvaluacionExternaDTO;
import com.pablo.ms_notas.dto.NotaDTO;
import com.pablo.ms_notas.model.Nota;
import com.pablo.ms_notas.repository.NotaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "serviciosExternos", fallbackMethod = "fallbackRegistrarNota")
    public ResponseEntity<Object> registrarNota(CrearNotaDTO solicitud) {
        if (solicitud.getValor() == null || solicitud.getValor() < 1.0 || solicitud.getValor() > 7.0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "La nota debe estar entre 1.0 y 7.0"));
        }
        AlumnoPerfilDTO alumno;
        try {
            alumno = restTemplate.getForObject(
                    "http://ms-perfiles/perfiles/alumnos/" + solicitud.getAlumnoId(), AlumnoPerfilDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe perfil de alumno con ID: " + solicitud.getAlumnoId()));
        }

        if (alumno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe perfil de alumno con ID: " + solicitud.getAlumnoId()));
        }

        EvaluacionExternaDTO evaluacion;
        try {
            evaluacion = restTemplate.getForObject(
                    "http://ms-evaluaciones/evaluaciones/" + solicitud.getEvaluacionId(), EvaluacionExternaDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe evaluacion con ID: " + solicitud.getEvaluacionId()));
        }

        if (evaluacion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe evaluacion con ID: " + solicitud.getEvaluacionId()));
        }

        Optional<Nota> existente = notaRepository
                .findFirstByAlumnoIdAndEvaluacionId(solicitud.getAlumnoId(), solicitud.getEvaluacionId());
        Nota nota = existente.orElse(new Nota());
        nota.setEvaluacionId(solicitud.getEvaluacionId());
        nota.setAlumnoId(solicitud.getAlumnoId());
        nota.setValor(solicitud.getValor());
        notaRepository.save(nota);

        return ResponseEntity.ok(Map.of("mensaje", "Nota registrada exitosamente."));
    }

    private ResponseEntity<Object> fallbackRegistrarNota(CrearNotaDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Uno o más servicios externos no están disponibles. Intente nuevamente más tarde."));
    }

    public List<NotaDTO> obtenerPorAlumno(Long alumnoId) {
        List<Nota> notas = notaRepository.findByAlumnoId(alumnoId);
        List<NotaDTO> resultado = new ArrayList<>();
        for (Nota n : notas) {
            NotaDTO dto = NotaDTO.desde(n);
            EvaluacionExternaDTO evaluacion = obtenerEvaluacion(n.getEvaluacionId());
            if (evaluacion != null) {
                dto.setTituloEvaluacion(evaluacion.getTitulo());
                dto.setAsignacionId(evaluacion.getAsignacionId());
                if (evaluacion.getAsignacionId() != null) {
                    AsignacionExternaDTO asignacion = obtenerAsignacion(evaluacion.getAsignacionId());
                    if (asignacion != null) {
                        dto.setNombreAsignatura(asignacion.getNombreAsignatura());
                    }
                }
            }
            resultado.add(dto);
        }
        return resultado;
    }

    public List<NotaDTO> obtenerPorEvaluacion(Long evaluacionId) {
        List<Nota> notas = notaRepository.findByEvaluacionId(evaluacionId);
        String titulo = obtenerTituloEvaluacion(evaluacionId);
        List<NotaDTO> resultado = new ArrayList<>();
        for (Nota n : notas) {
            NotaDTO dto = NotaDTO.desde(n);
            dto.setTituloEvaluacion(titulo);
            resultado.add(dto);
        }
        return resultado;
    }

    public ResponseEntity<Object> calcularPromedio(Long alumnoId) {
        List<Nota> notas = notaRepository.findByAlumnoId(alumnoId);
        if (notas.isEmpty()) {
            return ResponseEntity.ok(Map.of("alumnoId", alumnoId, "promedio", 0.0, "totalNotas", 0));
        }
        double suma = 0.0;
        for (Nota n : notas) {
            suma += n.getValor();
        }
        double promedio = suma / notas.size();
        return ResponseEntity.ok(Map.of("alumnoId", alumnoId, "promedio", promedio, "totalNotas", notas.size()));
    }

    public List<NotaDTO> obtenerPorAlumnoYAsignacion(Long alumnoId, Long asignacionId) {
        List<Nota> todasDelAlumno = notaRepository.findByAlumnoId(alumnoId);
        List<NotaDTO> resultado = new ArrayList<>();
        for (Nota n : todasDelAlumno) {
            EvaluacionExternaDTO evaluacion = obtenerEvaluacion(n.getEvaluacionId());
            if (evaluacion != null && asignacionId.equals(evaluacion.getAsignacionId())) {
                NotaDTO dto = NotaDTO.desde(n);
                dto.setTituloEvaluacion(evaluacion.getTitulo());
                resultado.add(dto);
            }
        }
        return resultado;
    }

    private String obtenerTituloEvaluacion(Long evaluacionId) {
        try {
            EvaluacionExternaDTO evaluacion = restTemplate.getForObject(
                    "http://ms-evaluaciones/evaluaciones/" + evaluacionId, EvaluacionExternaDTO.class);
            if (evaluacion != null) {
                return evaluacion.getTitulo();
            }
        } catch (Exception e) {
        }
        return null;
    }

    private EvaluacionExternaDTO obtenerEvaluacion(Long evaluacionId) {
        try {
            return restTemplate.getForObject(
                    "http://ms-evaluaciones/evaluaciones/" + evaluacionId, EvaluacionExternaDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    private AsignacionExternaDTO obtenerAsignacion(Long asignacionId) {
        try {
            return restTemplate.getForObject(
                    "http://ms-asignaturas/asignaciones/" + asignacionId, AsignacionExternaDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}
