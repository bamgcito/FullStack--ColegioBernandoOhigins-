package com.pablo.ms_evaluaciones.service;

import com.pablo.ms_evaluaciones.dto.AsignacionExternaDTO;
import com.pablo.ms_evaluaciones.dto.CrearEvaluacionDTO;
import com.pablo.ms_evaluaciones.dto.EvaluacionDTO;
import com.pablo.ms_evaluaciones.model.Evaluacion;
import com.pablo.ms_evaluaciones.repository.EvaluacionRepository;
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

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "serviciosExternos", fallbackMethod = "fallbackCrearEvaluacion")
    public ResponseEntity<Object> crearEvaluacion(CrearEvaluacionDTO solicitud) {
        AsignacionExternaDTO asignacion;
        try {
            asignacion = restTemplate.getForObject(
                    "http://ms-asignaturas/asignaciones/" + solicitud.getAsignacionId(), AsignacionExternaDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe asignacion docente con ID: " + solicitud.getAsignacionId()));
        }

        if (asignacion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe asignacion docente con ID: " + solicitud.getAsignacionId()));
        }

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTitulo(solicitud.getTitulo());
        evaluacion.setDescripcion(solicitud.getDescripcion());
        evaluacion.setAsignacionId(solicitud.getAsignacionId());
        evaluacionRepository.save(evaluacion);

        return ResponseEntity.ok(Map.of("mensaje", "Evaluacion creada exitosamente: " + solicitud.getTitulo()));
    }

    private ResponseEntity<Object> fallbackCrearEvaluacion(CrearEvaluacionDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Uno o más servicios externos no están disponibles. Intente nuevamente más tarde."));
    }

    public List<EvaluacionDTO> listarTodas() {
        List<Evaluacion> evaluaciones = evaluacionRepository.findAll();
        List<EvaluacionDTO> resultado = new ArrayList<>();
        for (Evaluacion e : evaluaciones) {
            resultado.add(EvaluacionDTO.desde(e));
        }
        return resultado;
    }

    public ResponseEntity<Object> buscarPorId(Long id) {
        Evaluacion e = evaluacionRepository.findById(id).orElse(null);
        if (e == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe evaluacion con ID: " + id));
        }
        return ResponseEntity.ok(EvaluacionDTO.desde(e));
    }

    public List<EvaluacionDTO> buscarPorAsignacion(Long asignacionId) {
        List<Evaluacion> evaluaciones = evaluacionRepository.findByAsignacionId(asignacionId);
        List<EvaluacionDTO> resultado = new ArrayList<>();
        for (Evaluacion e : evaluaciones) {
            resultado.add(EvaluacionDTO.desde(e));
        }
        return resultado;
    }
}
