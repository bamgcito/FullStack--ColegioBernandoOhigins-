package com.pablo.GESTIONACADEMICO.service;

import com.pablo.GESTIONACADEMICO.dto.AsignaturaDTO;
import com.pablo.GESTIONACADEMICO.dto.AsignaturaDetalleDTO;
import com.pablo.GESTIONACADEMICO.dto.CursoDTO;
import com.pablo.GESTIONACADEMICO.dto.EvaluacionDTO;
import com.pablo.GESTIONACADEMICO.dto.ProfesorDTO;
import com.pablo.GESTIONACADEMICO.dto.UsuarioDTO;
import com.pablo.GESTIONACADEMICO.model.Asignatura;
import com.pablo.GESTIONACADEMICO.model.AsignacionDocente;
import com.pablo.GESTIONACADEMICO.model.Evaluacion;
import com.pablo.GESTIONACADEMICO.repository.AsignacionDocenteRepository;
import com.pablo.GESTIONACADEMICO.repository.AsignaturaRepository;
import com.pablo.GESTIONACADEMICO.repository.EvaluacionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private AsignacionDocenteRepository asignacionDocenteRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String crearAsignatura(AsignaturaDTO dto) {
        if (asignaturaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            return "Ya existe una asignatura con el nombre: " + dto.getNombre();
        }

        Asignatura nueva = new Asignatura();
        nueva.setNombre(dto.getNombre());
        asignaturaRepository.save(nueva);
        return "Asignatura '" + dto.getNombre() + "' creada exitosamente.";
    }

    public List<AsignaturaDTO> listarAsignaturas() {
        List<Asignatura> asignaturas = asignaturaRepository.findAll();
        List<AsignaturaDTO> resultado = new ArrayList<>();
        for (Asignatura asignatura : asignaturas) {
            resultado.add(AsignaturaDTO.desde(asignatura));
        }
        return resultado;
    }

    public Object obtenerDetalle(Long asignaturaId, Long cursoId) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId).orElse(null);
        if (asignatura == null) {
            return "Asignatura no encontrada con ID: " + asignaturaId;
        }

        AsignacionDocente asignacion = asignacionDocenteRepository
                .findByCursoIdAndAsignaturaId(cursoId, asignaturaId).orElse(null);
        if (asignacion == null) {
            return "No existe una asignación para esta asignatura en el curso con ID: " + cursoId;
        }

        CursoDTO curso = null;
        try {
            curso = restTemplate.getForObject("http://GESTIONCURSO/cursos/" + cursoId, CursoDTO.class);
        } catch (Exception e) {

        }

        ProfesorDTO profesor = null;
        try {
            profesor = restTemplate.getForObject("http://GESTIONUSUARIO/usuarios/profesores/" + asignacion.getProfesorId(), ProfesorDTO.class);
        } catch (Exception e) {

        }

        List<Evaluacion> evaluaciones = evaluacionRepository.findByAsignacionDocenteId(asignacion.getId());
        List<EvaluacionDTO> evaluacionesDTO = new ArrayList<>();
        for (Evaluacion ev : evaluaciones) {
            evaluacionesDTO.add(EvaluacionDTO.desde(ev));
        }

        return AsignaturaDetalleDTO.desde(asignatura, asignacion, curso, profesor, evaluacionesDTO);
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackUsuario")
    private UsuarioDTO obtenerUsuario(Long id) {
        return restTemplate.getForObject("http://GESTIONUSUARIO/usuarios/" + id, UsuarioDTO.class);
    }

    private UsuarioDTO fallbackUsuario(Long id, Throwable t) {
        return null;
    }
}
