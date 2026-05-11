package com.pablo.GESTIONACADEMICO.service;

import com.pablo.GESTIONACADEMICO.dto.EvaluacionDTO;
import com.pablo.GESTIONACADEMICO.dto.UsuarioDTO;
import com.pablo.GESTIONACADEMICO.model.AsignacionDocente;
import com.pablo.GESTIONACADEMICO.model.Evaluacion;
import com.pablo.GESTIONACADEMICO.repository.AsignacionDocenteRepository;
import com.pablo.GESTIONACADEMICO.repository.EvaluacionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private AsignacionDocenteRepository asignacionDocenteRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String crearEvaluacion(EvaluacionDTO dto) {
        AsignacionDocente asignacion = asignacionDocenteRepository.findById(dto.getAsignacionDocenteId()).orElse(null);
        if (asignacion == null) {
            return "Asignación docente no encontrada con ID: " + dto.getAsignacionDocenteId();
        }

        evaluacionRepository.save(Evaluacion.desde(dto, asignacion));
        return "Evaluación creada exitosamente.";
    }

    public List<EvaluacionDTO> listarPorAsignacion(Long asignacionDocenteId) {
        List<Evaluacion> evaluaciones = evaluacionRepository.findByAsignacionDocenteId(asignacionDocenteId);
        List<EvaluacionDTO> resultado = new ArrayList<>();
        for (Evaluacion evaluacion : evaluaciones) {
            resultado.add(EvaluacionDTO.desde(evaluacion));
        }
        return resultado;
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackUsuario")
    private UsuarioDTO obtenerUsuario(Long id) {
        return restTemplate.getForObject("http://GESTIONUSUARIO/usuarios/" + id, UsuarioDTO.class);
    }

    private UsuarioDTO fallbackUsuario(Long id, Throwable t) {
        return null;
    }
}
