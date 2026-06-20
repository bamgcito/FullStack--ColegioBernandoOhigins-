package com.pablo.ms_asistencia.service;

import com.pablo.ms_asistencia.dto.AlumnoPerfilDTO;
import com.pablo.ms_asistencia.dto.AsignacionExternaDTO;
import com.pablo.ms_asistencia.dto.AsistenciaDTO;
import com.pablo.ms_asistencia.dto.CrearAsistenciaDTO;
import com.pablo.ms_asistencia.model.Asistencia;
import com.pablo.ms_asistencia.model.EstadoAsistencia;
import com.pablo.ms_asistencia.repository.AsistenciaRepository;
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
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "serviciosExternos", fallbackMethod = "fallbackRegistrarAsistencia")
    public ResponseEntity<Object> registrarAsistencia(CrearAsistenciaDTO solicitud) {
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

        Optional<Asistencia> existente = asistenciaRepository
                .findByAlumnoIdAndAsignacionIdAndFecha(
                        solicitud.getAlumnoId(), solicitud.getAsignacionId(), solicitud.getFecha());

        Asistencia asistencia = existente.orElse(new Asistencia());
        asistencia.setAlumnoId(solicitud.getAlumnoId());
        asistencia.setAsignacionId(solicitud.getAsignacionId());
        asistencia.setEstado(solicitud.getEstado());
        asistencia.setFecha(solicitud.getFecha());
        asistenciaRepository.save(asistencia);

        return ResponseEntity.ok(Map.of("mensaje", "Asistencia registrada exitosamente."));
    }

    private ResponseEntity<Object> fallbackRegistrarAsistencia(CrearAsistenciaDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Uno o más servicios externos no están disponibles. Intente nuevamente más tarde."));
    }

    public List<AsistenciaDTO> obtenerPorAlumno(Long alumnoId) {
        List<Asistencia> asistencias = asistenciaRepository.findByAlumnoId(alumnoId);
        List<AsistenciaDTO> resultado = new ArrayList<>();
        for (Asistencia a : asistencias) {
            resultado.add(AsistenciaDTO.desde(a));
        }
        return resultado;
    }

    public List<AsistenciaDTO> obtenerPorAsignacion(Long asignacionId) {
        List<Asistencia> asistencias = asistenciaRepository.findByAsignacionId(asignacionId);
        List<AsistenciaDTO> resultado = new ArrayList<>();
        for (Asistencia a : asistencias) {
            resultado.add(AsistenciaDTO.desde(a));
        }
        return resultado;
    }

    public ResponseEntity<Object> calcularPorcentaje(Long alumnoId) {
        List<Asistencia> asistencias = asistenciaRepository.findByAlumnoId(alumnoId);
        if (asistencias.isEmpty()) {
            return ResponseEntity.ok(Map.of("alumnoId", alumnoId, "porcentaje", 0.0, "totalClases", 0));
        }
        long presentes = 0;
        for (Asistencia a : asistencias) {
            if (EstadoAsistencia.PRESENTE.equals(a.getEstado()) || EstadoAsistencia.ATRASADO.equals(a.getEstado())) {
                presentes++;
            }
        }
        double porcentaje = (presentes * 100.0) / asistencias.size();
        return ResponseEntity.ok(Map.of(
                "alumnoId", alumnoId,
                "porcentaje", porcentaje,
                "totalClases", asistencias.size(),
                "presentes", presentes
        ));
    }
}
