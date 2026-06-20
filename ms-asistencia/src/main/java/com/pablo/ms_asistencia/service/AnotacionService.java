package com.pablo.ms_asistencia.service;

import com.pablo.ms_asistencia.dto.AlumnoPerfilDTO;
import com.pablo.ms_asistencia.dto.AnotacionDTO;
import com.pablo.ms_asistencia.dto.AsignacionExternaDTO;
import com.pablo.ms_asistencia.dto.CrearAnotacionDTO;
import com.pablo.ms_asistencia.model.Anotacion;
import com.pablo.ms_asistencia.repository.AnotacionRepository;
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
public class AnotacionService {

    @Autowired
    private AnotacionRepository anotacionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "serviciosExternos", fallbackMethod = "fallbackRegistrarAnotacion")
    public ResponseEntity<Object> registrarAnotacion(CrearAnotacionDTO solicitud) {
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

        Anotacion anotacion = new Anotacion();
        anotacion.setAlumnoId(solicitud.getAlumnoId());
        anotacion.setAsignacionId(solicitud.getAsignacionId());
        anotacion.setTipo(solicitud.getTipo());
        anotacion.setDescripcion(solicitud.getDescripcion());
        anotacionRepository.save(anotacion);

        return ResponseEntity.ok(Map.of("mensaje", "Anotacion registrada exitosamente."));
    }

    private ResponseEntity<Object> fallbackRegistrarAnotacion(CrearAnotacionDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Uno o más servicios externos no están disponibles. Intente nuevamente más tarde."));
    }

    public List<AnotacionDTO> obtenerPorAlumno(Long alumnoId) {
        List<Anotacion> anotaciones = anotacionRepository.findByAlumnoId(alumnoId);
        List<AnotacionDTO> resultado = new ArrayList<>();
        for (Anotacion a : anotaciones) {
            resultado.add(AnotacionDTO.desde(a));
        }
        return resultado;
    }

    public List<AnotacionDTO> obtenerPorAsignacion(Long asignacionId) {
        List<Anotacion> anotaciones = anotacionRepository.findByAsignacionId(asignacionId);
        List<AnotacionDTO> resultado = new ArrayList<>();
        for (Anotacion a : anotaciones) {
            resultado.add(AnotacionDTO.desde(a));
        }
        return resultado;
    }
}
