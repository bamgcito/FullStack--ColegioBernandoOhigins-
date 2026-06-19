package com.pablo.ms_perfiles.service;

import com.pablo.ms_perfiles.dto.AlumnoDTO;
import com.pablo.ms_perfiles.dto.ApoderadoDTO;
import com.pablo.ms_perfiles.dto.AsociarDTO;
import com.pablo.ms_perfiles.dto.UsuarioDTO;
import com.pablo.ms_perfiles.model.Alumno;
import com.pablo.ms_perfiles.model.AlumnoApoderado;
import com.pablo.ms_perfiles.model.Apoderado;
import com.pablo.ms_perfiles.repository.AlumnoApoderadoRepository;
import com.pablo.ms_perfiles.repository.AlumnoRepository;
import com.pablo.ms_perfiles.repository.ApoderadoRepository;
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
public class ApoderadoService {

    @Autowired
    private ApoderadoRepository apoderadoRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AlumnoApoderadoRepository alumnoApoderadoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackCrearApoderado")
    public ResponseEntity<Object> crearApoderado(ApoderadoDTO solicitud) {
        UsuarioDTO usuario = restTemplate.getForObject(
                "http://ms-usuarios/usuarios/rut/" + solicitud.getRut(), UsuarioDTO.class);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe usuario con RUT: " + solicitud.getRut()));
        }

        if (apoderadoRepository.existsByUsuarioId(usuario.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un perfil de apoderado para el usuario con RUT: " + solicitud.getRut()));
        }

        if (apoderadoRepository.existsByRut(solicitud.getRut())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un perfil de apoderado con el RUT: " + solicitud.getRut()));
        }

        Apoderado apoderado = new Apoderado();
        apoderado.setUsuarioId(usuario.getId());
        apoderado.setRut(solicitud.getRut());
        apoderado.setNombre(solicitud.getNombre());
        apoderado.setApellido(solicitud.getApellido());
        apoderado.setTelefono(solicitud.getTelefono());
        apoderadoRepository.save(apoderado);

        return ResponseEntity.ok(Map.of("mensaje", "Perfil de apoderado creado exitosamente para el RUT: " + solicitud.getRut()));
    }

    private ResponseEntity<Object> fallbackCrearApoderado(ApoderadoDTO solicitud, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "El servicio de usuarios no está disponible. Intente nuevamente más tarde."));
    }

    public ApoderadoDTO buscarPorUsuarioId(Long usuarioId) {
        Apoderado a = apoderadoRepository.findByUsuarioId(usuarioId).orElse(null);
        if (a == null) return null;
        return ApoderadoDTO.desde(a);
    }

    public ResponseEntity<Object> asociar(AsociarDTO solicitud) {
        Alumno alumno = alumnoRepository.findByRut(solicitud.getRutAlumno()).orElse(null);
        if (alumno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe perfil de alumno con RUT: " + solicitud.getRutAlumno()));
        }

        Apoderado apoderado = apoderadoRepository.findByRut(solicitud.getRutApoderado()).orElse(null);
        if (apoderado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe perfil de apoderado con RUT: " + solicitud.getRutApoderado()));
        }

        if (alumnoApoderadoRepository.existsByAlumnoAndApoderado(alumno, apoderado)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Esta asociación ya existe."));
        }

        AlumnoApoderado asociacion = new AlumnoApoderado();
        asociacion.setAlumno(alumno);
        asociacion.setApoderado(apoderado);
        alumnoApoderadoRepository.save(asociacion);

        return ResponseEntity.ok(Map.of("mensaje", "Apoderado asociado al alumno exitosamente."));
    }

    public void eliminarPorUsuarioId(Long usuarioId) {
        apoderadoRepository.deleteByUsuarioId(usuarioId);
    }

    public List<AlumnoDTO> listarAlumnosPorApoderado(Long apoderadoUsuarioId) {
        Apoderado apoderado = apoderadoRepository.findByUsuarioId(apoderadoUsuarioId).orElse(null);
        if (apoderado == null) return new ArrayList<>();

        List<AlumnoApoderado> asociaciones = alumnoApoderadoRepository.findByApoderado(apoderado);
        List<AlumnoDTO> resultado = new ArrayList<>();
        for (AlumnoApoderado aa : asociaciones) {
            resultado.add(AlumnoDTO.desde(aa.getAlumno()));
        }
        return resultado;
    }
}
