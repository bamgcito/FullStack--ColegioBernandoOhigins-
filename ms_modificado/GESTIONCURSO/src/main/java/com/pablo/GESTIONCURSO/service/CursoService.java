package com.pablo.GESTIONCURSO.service;

import com.pablo.GESTIONCURSO.dto.AlumnoDetalleDTO;
import com.pablo.GESTIONCURSO.dto.AsignarAlumnoDTO;
import com.pablo.GESTIONCURSO.dto.AsignarProfesorJefeDTO;
import com.pablo.GESTIONCURSO.dto.CursoDTO;
import com.pablo.GESTIONCURSO.dto.ProfesorDTO;
import com.pablo.GESTIONCURSO.dto.UsuarioDTO;
import com.pablo.GESTIONCURSO.model.Curso;
import com.pablo.GESTIONCURSO.model.CursoAlumno;
import com.pablo.GESTIONCURSO.repository.CursoAlumnoRepository;
import com.pablo.GESTIONCURSO.repository.CursoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CursoService {

    @Autowired private CursoRepository cursoRepository;
    @Autowired private CursoAlumnoRepository cursoAlumnoRepository;
    @Autowired private RestTemplate restTemplate;

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackCrearCurso")
    public String crearCurso(CursoDTO dto) {
        if (cursoRepository.existsByNivelAndLetraAndAnio(dto.getNivel(), dto.getLetra(), dto.getAnio())) {
            return "Ya existe un curso con nivel " + dto.getNivel() + ", letra " + dto.getLetra() + " y año " + dto.getAnio() + ".";
        }
        cursoRepository.save(Curso.desde(dto));
        return "Curso creado exitosamente.";
    }

    public List<CursoDTO> listarCursos() {
        List<Curso> cursos = cursoRepository.findAll();
        List<CursoDTO> lista = new ArrayList<>();
        for (Curso c : cursos) {
            CursoDTO dto = CursoDTO.desde(c);
            if (c.getProfesorJefeId() != null) {
                ProfesorDTO profesor = restTemplate.getForObject(
                    "http://GESTIONUSUARIO/usuarios/profesores/" + c.getProfesorJefeId(), ProfesorDTO.class);
                if (profesor != null) {
                    dto.setNombreProfesorJefe(profesor.getNombre() + " " + profesor.getApellido());
                }
            }
            List<CursoAlumno> relaciones = cursoAlumnoRepository.findByCursoId(c.getId());
            List<AlumnoDetalleDTO> alumnos = new ArrayList<>();
            for (CursoAlumno ca : relaciones) {
                UsuarioDTO usuario = restTemplate.getForObject(
                    "http://GESTIONUSUARIO/usuarios/alumnos/id/" + ca.getAlumnoId(), UsuarioDTO.class);
                if (usuario != null) {
                    alumnos.add(AlumnoDetalleDTO.desde(ca.getAlumnoId(), usuario));
                }
            }
            dto.setAlumnos(alumnos);
            lista.add(dto);
        }
        return lista;
    }

    public CursoDTO buscarPorId(Long id) {
        Curso encontrado = cursoRepository.findById(id).orElse(null);
        if (encontrado == null) return null;
        return CursoDTO.desde(encontrado);
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackAsignarAlumno")
    public String asignarAlumno(AsignarAlumnoDTO dto) {
        UsuarioDTO alumno = restTemplate.getForObject(
            "http://GESTIONUSUARIO/usuarios/rut/" + dto.getAlumnoRut(), UsuarioDTO.class);
        if (alumno == null) {
            return "Alumno no encontrado con RUT: " + dto.getAlumnoRut();
        }
        if (!cursoRepository.existsById(dto.getCursoId())) {
            return "Curso no encontrado con ID: " + dto.getCursoId();
        }
        if (cursoAlumnoRepository.existsByCursoIdAndAlumnoId(dto.getCursoId(), alumno.getId())) {
            return "El alumno con RUT " + dto.getAlumnoRut() + " ya está asignado a este curso.";
        }
        Curso curso = cursoRepository.findById(dto.getCursoId()).get();
        CursoAlumno relacion = new CursoAlumno();
        relacion.setCurso(curso);
        relacion.setAlumnoId(alumno.getId());
        cursoAlumnoRepository.save(relacion);
        return "Alumno asignado exitosamente al curso.";
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackAsignarProfesor")
    public String asignarProfesorJefe(Long cursoId, AsignarProfesorJefeDTO dto) {
        if (!cursoRepository.existsById(cursoId)) {
            return "Curso no encontrado con ID: " + cursoId;
        }
        UsuarioDTO profesor = restTemplate.getForObject(
            "http://GESTIONUSUARIO/usuarios/rut/" + dto.getProfesorJefeRut(), UsuarioDTO.class);
        if (profesor == null) {
            return "Profesor no encontrado con RUT: " + dto.getProfesorJefeRut();
        }
        if (cursoRepository.existsByProfesorJefeId(profesor.getId())) {
            return "El profesor con RUT " + dto.getProfesorJefeRut() + " ya es profesor jefe de otro curso.";
        }
        Curso curso = cursoRepository.findById(cursoId).get();
        curso.setProfesorJefeId(profesor.getId());
        cursoRepository.save(curso);
        return "Profesor jefe asignado exitosamente al curso.";
    }

    private String fallbackCrearCurso(CursoDTO dto, Throwable t) {
        return "El servicio de usuarios no está disponible. Intente nuevamente más tarde.";
    }

    private String fallbackAsignarAlumno(AsignarAlumnoDTO dto, Throwable t) {
        return "El servicio de usuarios no está disponible. Intente nuevamente más tarde.";
    }

    private String fallbackAsignarProfesor(Long cursoId, AsignarProfesorJefeDTO dto, Throwable t) {
        return "El servicio de usuarios no está disponible. Intente nuevamente más tarde.";
    }
}
