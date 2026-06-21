package com.pablo.ms_perfiles.service;

import com.pablo.ms_perfiles.dto.ProfesorDTO;
import com.pablo.ms_perfiles.dto.UsuarioDTO;
import com.pablo.ms_perfiles.model.Profesor;
import com.pablo.ms_perfiles.repository.ProfesorRepository;
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
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackCrearProfesor")
    public ResponseEntity<Object> crearProfesor(ProfesorDTO solicitud) {
        UsuarioDTO usuario = restTemplate.getForObject(
                "http://ms-usuarios/usuarios/rut/" + solicitud.getRut(), UsuarioDTO.class);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe usuario con RUT: " + solicitud.getRut()));
        }

        if (profesorRepository.existsByUsuarioId(usuario.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un perfil de profesor para el usuario con RUT: " + solicitud.getRut()));
        }

        if (profesorRepository.existsByRut(solicitud.getRut())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un perfil de profesor con el RUT: " + solicitud.getRut()));
        }

        Profesor profesor = new Profesor();
        profesor.setUsuarioId(usuario.getId());
        profesor.setRut(solicitud.getRut());
        profesor.setNombre(solicitud.getNombre());
        profesor.setApellido(solicitud.getApellido());
        profesor.setEspecialidad(solicitud.getEspecialidad());
        profesorRepository.save(profesor);

        return ResponseEntity.ok(Map.of("mensaje", "Perfil de profesor creado exitosamente para el RUT: " + solicitud.getRut()));
    }

    private ResponseEntity<Object> fallbackCrearProfesor(ProfesorDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "El servicio de usuarios no está disponible. Intente nuevamente más tarde."));
    }

    public ProfesorDTO buscarPorUsuarioId(Long usuarioId) {
        Profesor p = profesorRepository.findByUsuarioId(usuarioId).orElse(null);
        if (p == null) return null;
        return ProfesorDTO.desde(p);
    }

    public ProfesorDTO buscarPorRut(String rut) {
        Profesor p = profesorRepository.findByRut(rut).orElse(null);
        if (p == null) return null;
        return ProfesorDTO.desde(p);
    }

    public void eliminarPorUsuarioId(Long usuarioId) {
        profesorRepository.deleteByUsuarioId(usuarioId);
    }

    public List<ProfesorDTO> listarTodos() {
        List<Profesor> todos = profesorRepository.findAll();
        List<ProfesorDTO> resultado = new ArrayList<>();
        for (Profesor p : todos) {
            resultado.add(ProfesorDTO.desde(p));
        }
        return resultado;
    }
}
