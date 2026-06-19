package com.pablo.ms_perfiles.service;

import com.pablo.ms_perfiles.dto.AlumnoDTO;
import com.pablo.ms_perfiles.dto.ApoderadoDTO;
import com.pablo.ms_perfiles.dto.UsuarioDTO;
import com.pablo.ms_perfiles.model.Alumno;
import com.pablo.ms_perfiles.model.AlumnoApoderado;
import com.pablo.ms_perfiles.repository.AlumnoApoderadoRepository;
import com.pablo.ms_perfiles.repository.AlumnoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AlumnoApoderadoRepository alumnoApoderadoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackCrearAlumno")
    public ResponseEntity<Object> crearAlumno(AlumnoDTO solicitud) {
        UsuarioDTO usuario = restTemplate.getForObject(
                "http://ms-usuarios/usuarios/rut/" + solicitud.getRut(), UsuarioDTO.class);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe usuario con RUT: " + solicitud.getRut()));
        }

        if (alumnoRepository.existsByUsuarioId(usuario.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un perfil de alumno para el usuario con RUT: " + solicitud.getRut()));
        }

        if (alumnoRepository.existsByRut(solicitud.getRut())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un perfil de alumno con el RUT: " + solicitud.getRut()));
        }

        Alumno alumno = new Alumno();
        alumno.setUsuarioId(usuario.getId());
        alumno.setRut(solicitud.getRut());
        alumno.setNombre(solicitud.getNombre());
        alumno.setApellido(solicitud.getApellido());
        alumno.setFechaNacimiento(solicitud.getFechaNacimiento());
        alumnoRepository.save(alumno);

        return ResponseEntity.ok(Map.of("mensaje", "Perfil de alumno creado exitosamente para el RUT: " + solicitud.getRut()));
    }

    private ResponseEntity<Object> fallbackCrearAlumno(AlumnoDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "El servicio de usuarios no está disponible. Intente nuevamente más tarde."));
    }

    public AlumnoDTO buscarPorUsuarioId(Long usuarioId) {
        Alumno a = alumnoRepository.findByUsuarioId(usuarioId).orElse(null);
        if (a == null) return null;
        return AlumnoDTO.desde(a);
    }

    public AlumnoDTO buscarPorRut(String rut) {
        Alumno a = alumnoRepository.findByRut(rut).orElse(null);
        if (a == null) return null;
        return AlumnoDTO.desde(a);
    }

    public void eliminarPorUsuarioId(Long usuarioId) {
        alumnoRepository.deleteByUsuarioId(usuarioId);
    }

    public List<AlumnoDTO> listarTodos() {
        List<Alumno> todos = alumnoRepository.findAll();
        List<AlumnoDTO> resultado = new ArrayList<>();
        for (Alumno a : todos) {
            resultado.add(AlumnoDTO.desde(a));
        }
        return resultado;
    }

    public List<ApoderadoDTO> listarApoderadosPorAlumno(Long usuarioId) {
        Alumno alumno = alumnoRepository.findByUsuarioId(usuarioId).orElse(null);
        if (alumno == null) return new ArrayList<>();

        List<AlumnoApoderado> asociaciones = alumnoApoderadoRepository.findByAlumno(alumno);
        List<ApoderadoDTO> resultado = new ArrayList<>();
        for (AlumnoApoderado aa : asociaciones) {
            resultado.add(ApoderadoDTO.desde(aa.getApoderado()));
        }
        return resultado;
    }
}
