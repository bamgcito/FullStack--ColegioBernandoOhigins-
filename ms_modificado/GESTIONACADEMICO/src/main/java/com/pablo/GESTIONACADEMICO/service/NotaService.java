package com.pablo.GESTIONACADEMICO.service;

import com.pablo.GESTIONACADEMICO.dto.AlumnoDTO;
import com.pablo.GESTIONACADEMICO.dto.NotaDTO;
import com.pablo.GESTIONACADEMICO.dto.PromedioAlumnoDTO;
import com.pablo.GESTIONACADEMICO.dto.PromedioAsignaturaDTO;
import com.pablo.GESTIONACADEMICO.dto.UsuarioDTO;
import com.pablo.GESTIONACADEMICO.model.Evaluacion;
import com.pablo.GESTIONACADEMICO.model.Nota;
import com.pablo.GESTIONACADEMICO.repository.EvaluacionRepository;
import com.pablo.GESTIONACADEMICO.repository.NotaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    public String registrarNota(NotaDTO dto, String auth) {
        UsuarioDTO alumno = obtenerUsuarioPorRut(dto.getRutAlumno(), auth);
        if (alumno == null) {
            return "No existe un usuario con RUT: " + dto.getRutAlumno();
        }
        Evaluacion evaluacion = evaluacionRepository.findById(dto.getEvaluacionId()).orElse(null);
        if (evaluacion == null) {
            return "Evaluación no encontrada con ID: " + dto.getEvaluacionId();
        }
        if (notaRepository.existsByAlumnoIdAndEvaluacionId(alumno.getId(), dto.getEvaluacionId())) {
            return "Ya existe una nota para este alumno en esta evaluación.";
        }
        notaRepository.save(Nota.desde(dto, evaluacion, alumno.getId()));
        return "Nota registrada exitosamente.";
    }

    public Object listarNotasAlumno(String rutAlumno, String auth) {
        UsuarioDTO alumnoUsuario = obtenerUsuarioPorRut(rutAlumno, auth);
        if (alumnoUsuario == null) {
            return "No existe un usuario con RUT: " + rutAlumno;
        }
        AlumnoDTO alumno = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", auth);
            alumno = restTemplate.exchange(
                "http://GESTIONUSUARIO/usuarios/alumnos/id/" + alumnoUsuario.getId(),
                HttpMethod.GET, new HttpEntity<>(headers), AlumnoDTO.class).getBody();
        } catch (Exception e) { /* servicio no disponible */ }

        List<Nota> notas = notaRepository.findByAlumnoId(alumnoUsuario.getId());
        List<NotaDTO> resultado = new ArrayList<>();
        for (Nota nota : notas) {
            NotaDTO notaDTO = NotaDTO.desde(nota);
            if (alumno != null) {
                notaDTO.setRutAlumno(alumno.getRut());
                notaDTO.setNombre(alumno.getNombre());
                notaDTO.setApellido(alumno.getApellido());
            }
            resultado.add(notaDTO);
        }
        return resultado;
    }

    public Object calcularPromedioAlumno(String rutAlumno, String auth) {
        UsuarioDTO alumno = obtenerUsuarioPorRut(rutAlumno, auth);
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

    public List<NotaDTO> listarPorEvaluacion(Long evaluacionId, String auth) {
        List<Nota> notas = notaRepository.findByEvaluacionId(evaluacionId);
        List<NotaDTO> resultado = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", auth);
        for (Nota nota : notas) {
            NotaDTO notaDTO = NotaDTO.desde(nota);
            try {
                AlumnoDTO alumno = restTemplate.exchange(
                    "http://GESTIONUSUARIO/usuarios/alumnos/id/" + nota.getAlumnoId(),
                    HttpMethod.GET, new HttpEntity<>(headers), AlumnoDTO.class).getBody();
                if (alumno != null) {
                    notaDTO.setRutAlumno(alumno.getRut());
                    notaDTO.setNombre(alumno.getNombre());
                    notaDTO.setApellido(alumno.getApellido());
                }
            } catch (Exception e) { /* servicio no disponible */ }
            resultado.add(notaDTO);
        }
        return resultado;
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackUsuarioPorRut")
    UsuarioDTO obtenerUsuarioPorRut(String rut, String auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", auth);
        return restTemplate.exchange(
            "http://GESTIONUSUARIO/usuarios/rut/" + rut,
            HttpMethod.GET, new HttpEntity<>(headers), UsuarioDTO.class).getBody();
    }

    UsuarioDTO fallbackUsuarioPorRut(String rut, String auth, Throwable t) { return null; }
}