package com.pablo.ms_horarios.service;

import com.pablo.ms_horarios.dto.AsignacionExternaDTO;
import com.pablo.ms_horarios.dto.CrearHorarioDTO;
import com.pablo.ms_horarios.dto.HorarioDTO;
import com.pablo.ms_horarios.model.Horario;
import com.pablo.ms_horarios.repository.HorarioRepository;
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
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final List<String> DIAS_VALIDOS = List.of("LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES");

    @CircuitBreaker(name = "serviciosExternos", fallbackMethod = "fallbackCrearHorario")
    public ResponseEntity<Object> crearHorario(CrearHorarioDTO solicitud) {
        if (solicitud.getDiaSemana() == null || !DIAS_VALIDOS.contains(solicitud.getDiaSemana())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Día no válido. Use: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES"));
        }

        if (solicitud.getBloque() == null || solicitud.getBloque() < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El bloque debe ser un número positivo"));
        }

        if (solicitud.getHoraInicio() == null || solicitud.getHoraFin() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "La hora de inicio y fin son obligatorias"));
        }

        AsignacionExternaDTO asignacion;
        try {
            asignacion = restTemplate.getForObject(
                    "http://ms-asignaturas/asignaciones/" + solicitud.getAsignacionDocenteId(),
                    AsignacionExternaDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe asignación docente con ID: " + solicitud.getAsignacionDocenteId()));
        }

        if (asignacion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe asignación docente con ID: " + solicitud.getAsignacionDocenteId()));
        }

        Long profesorId = asignacion.getProfesorId();
        Long cursoId = asignacion.getCursoId();

        List<Horario> existentes = horarioRepository.findByDiaSemanaAndBloque(
                solicitud.getDiaSemana(), solicitud.getBloque());

        for (Horario h : existentes) {
            if (profesorId.equals(h.getProfesorId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Choque de profesor: ya tiene un bloque asignado el "
                                + solicitud.getDiaSemana() + " en el bloque " + solicitud.getBloque()));
            }
            if (cursoId.equals(h.getCursoId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Choque de curso: el curso ya tiene una asignatura el "
                                + solicitud.getDiaSemana() + " en el bloque " + solicitud.getBloque()));
            }
        }

        Horario horario = new Horario();
        horario.setAsignacionDocenteId(solicitud.getAsignacionDocenteId());
        horario.setProfesorId(profesorId);
        horario.setCursoId(cursoId);
        horario.setDiaSemana(solicitud.getDiaSemana());
        horario.setBloque(solicitud.getBloque());
        horario.setHoraInicio(solicitud.getHoraInicio());
        horario.setHoraFin(solicitud.getHoraFin());
        horarioRepository.save(horario);

        return ResponseEntity.ok(Map.of("mensaje", "Horario creado exitosamente"));
    }

    private ResponseEntity<Object> fallbackCrearHorario(CrearHorarioDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Uno o más servicios externos no están disponibles. Intente nuevamente más tarde."));
    }

    public ResponseEntity<Object> eliminarHorario(Long id) {
        Horario horario = horarioRepository.findById(id).orElse(null);
        if (horario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe horario con ID: " + id));
        }
        horarioRepository.delete(horario);
        return ResponseEntity.ok(Map.of("mensaje", "Horario eliminado exitosamente"));
    }

    public List<HorarioDTO> obtenerPorCurso(Long cursoId) {
        List<Horario> horarios = horarioRepository.findByCursoId(cursoId);
        List<HorarioDTO> resultado = new ArrayList<>();
        for (Horario h : horarios) {
            HorarioDTO dto = HorarioDTO.desde(h);
            AsignacionExternaDTO asignacion = obtenerAsignacion(h.getAsignacionDocenteId());
            if (asignacion != null) {
                dto.setNombreAsignatura(asignacion.getNombreAsignatura());
            }
            resultado.add(dto);
        }
        return resultado;
    }

    public List<HorarioDTO> obtenerPorProfesor(Long profesorId) {
        List<Horario> horarios = horarioRepository.findByProfesorId(profesorId);
        List<HorarioDTO> resultado = new ArrayList<>();
        for (Horario h : horarios) {
            HorarioDTO dto = HorarioDTO.desde(h);
            AsignacionExternaDTO asignacion = obtenerAsignacion(h.getAsignacionDocenteId());
            if (asignacion != null) {
                dto.setNombreAsignatura(asignacion.getNombreAsignatura());
                dto.setNombreCurso(asignacion.getNombreCurso());
            }
            resultado.add(dto);
        }
        return resultado;
    }

    public List<HorarioDTO> obtenerPorAsignacion(Long asignacionId) {
        List<Horario> horarios = horarioRepository.findByAsignacionDocenteId(asignacionId);
        List<HorarioDTO> resultado = new ArrayList<>();
        for (Horario h : horarios) {
            resultado.add(HorarioDTO.desde(h));
        }
        return resultado;
    }

    private AsignacionExternaDTO obtenerAsignacion(Long asignacionId) {
        try {
            return restTemplate.getForObject(
                    "http://ms-asignaturas/asignaciones/" + asignacionId,
                    AsignacionExternaDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}