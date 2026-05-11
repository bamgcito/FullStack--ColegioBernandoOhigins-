package com.pablo.GESTIONACADEMICO.service;

import com.pablo.GESTIONACADEMICO.dto.AlumnoDTO;
import com.pablo.GESTIONACADEMICO.dto.NotaDTO;
import com.pablo.GESTIONACADEMICO.dto.PromedioAlumnoDTO;
import com.pablo.GESTIONACADEMICO.dto.PromedioAsignaturaDTO;
import com.pablo.GESTIONACADEMICO.dto.UsuarioDTO;
import com.pablo.GESTIONACADEMICO.model.AsignacionDocente;
import com.pablo.GESTIONACADEMICO.model.Evaluacion;
import com.pablo.GESTIONACADEMICO.model.Nota;
import com.pablo.GESTIONACADEMICO.repository.EvaluacionRepository;
import com.pablo.GESTIONACADEMICO.repository.NotaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotaService {

    @Autowired private NotaRepository notaRepository;
    @Autowired private EvaluacionRepository evaluacionRepository;
    @Autowired private RestTemplate restTemplate;

    public String registrarNota(NotaDTO dto) {
        UsuarioDTO alumno = obtenerUsuarioPorRut(dto.getRutAlumno());
        if (alumno == null) {
            return "No existe un usuario con RUT: " + dto.getRutAlumno();
        }
        Evaluacion evaluacion = evaluacionRepository.findById(dto.getEvaluacionId()).orElse(null);
        if (evaluacion == null) {
            return "Evaluación no encontrada con ID: " + dto.getEvaluacionId();
        }
        // ← eliminada la validación de solicitanteId que causaba el error
        if (notaRepository.existsByAlumnoIdAndEvaluacionId(alumno.getId(), dto.getEvaluacionId())) {
            return "Ya existe una nota para este alumno en esta evaluación.";
        }
        notaRepository.save(Nota.desde(dto, evaluacion, alumno.getId()));
        return "Nota registrada exitosamente.";
    }

    // Devuelve notas enriquecidas con nombre/apellido del alumno,
    // nombre de la evaluación y nombre de la asignatura — para el front
    public Object listarNotasAlumno(String rutAlumno) {
        UsuarioDTO alumnoUsuario = obtenerUsuarioPorRut(rutAlumno);
        if (alumnoUsuario == null) {
            return "No existe un usuario con RUT: " + rutAlumno;
        }
        AlumnoDTO alumno = restTemplate.getForObject(
            "http://GESTIONUSUARIO/usuarios/alumnos/id/" + alumnoUsuario.getId(), AlumnoDTO.class);

        List<Nota> notas = notaRepository.findByAlumnoId(alumnoUsuario.getId());
        List<NotaDTO> resultado = new ArrayList<>();
        for (Nota nota : notas) {
            NotaDTO notaDTO = NotaDTO.desde(nota); // ya incluye evalNombre, fechaEvaluacion, nombreAsignatura
            if (alumno != null) {
                notaDTO.setRutAlumno(alumno.getRut());
                notaDTO.setNombre(alumno.getNombre());
                notaDTO.setApellido(alumno.getApellido());
            }
            resultado.add(notaDTO);
        }
        return resultado;
    }

    public Object calcularPromedioAlumno(String rutAlumno) {
        UsuarioDTO alumno = obtenerUsuarioPorRut(rutAlumno);
        if (alumno == null) {
            return "No existe un usuario con RUT: " + rutAlumno;
        }
        List<Nota> notas = notaRepository.findByAlumnoId(alumno.getId());
        if (notas == null || notas.isEmpty()) {
            return "El alumno no tiene notas registradas.";
        }

        Map<Long, List<Double>> notasPorAsignatura = new HashMap<>();
        Map<Long, String> nombresPorAsignatura = new HashMap<>();
        for (Nota nota : notas) {
            Long asignaturaId = nota.getEvaluacion().getAsignacionDocente().getAsignatura().getId();
            String nombreAsignatura = nota.getEvaluacion().getAsignacionDocente().getAsignatura().getNombre();
            notasPorAsignatura.computeIfAbsent(asignaturaId, k -> new ArrayList<>()).add(nota.getNota());
            nombresPorAsignatura.put(asignaturaId, nombreAsignatura);
        }

        double sumaPromedios = 0.0;
        List<PromedioAsignaturaDTO> promediosPorAsignatura = new ArrayList<>();
        for (Map.Entry<Long, List<Double>> entry : notasPorAsignatura.entrySet()) {
            double promedioAsig = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            sumaPromedios += promedioAsig;
            PromedioAsignaturaDTO promedioDTO = new PromedioAsignaturaDTO();
            promedioDTO.setNombreAsignatura(nombresPorAsignatura.get(entry.getKey()));
            promedioDTO.setPromedio(Math.round(promedioAsig * 10.0) / 10.0);
            promediosPorAsignatura.add(promedioDTO);
        }

        PromedioAlumnoDTO resultado = new PromedioAlumnoDTO();
        resultado.setRutAlumno(rutAlumno);
        resultado.setPromedioGeneral(Math.round((sumaPromedios / notasPorAsignatura.size()) * 10.0) / 10.0);
        resultado.setPromediosPorAsignatura(promediosPorAsignatura);
        return resultado;
    }

    public List<NotaDTO> listarPorEvaluacion(Long evaluacionId) {
        List<Nota> notas = notaRepository.findByEvaluacionId(evaluacionId);
        List<NotaDTO> resultado = new ArrayList<>();
        for (Nota nota : notas) {
            NotaDTO notaDTO = NotaDTO.desde(nota);
            AlumnoDTO alumno = restTemplate.getForObject(
                "http://GESTIONUSUARIO/usuarios/alumnos/id/" + nota.getAlumnoId(), AlumnoDTO.class);
            if (alumno != null) {
                notaDTO.setRutAlumno(alumno.getRut());
                notaDTO.setNombre(alumno.getNombre());
                notaDTO.setApellido(alumno.getApellido());
            }
            resultado.add(notaDTO);
        }
        return resultado;
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackUsuarioPorRut")
    UsuarioDTO obtenerUsuarioPorRut(String rut) {
        return restTemplate.getForObject("http://GESTIONUSUARIO/usuarios/rut/" + rut, UsuarioDTO.class);
    }

    UsuarioDTO fallbackUsuarioPorRut(String rut, Throwable t) { return null; }
}
