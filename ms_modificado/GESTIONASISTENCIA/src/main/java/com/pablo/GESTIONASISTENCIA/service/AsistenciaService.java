package com.pablo.GESTIONASISTENCIA.service;

import com.pablo.GESTIONASISTENCIA.dto.AsistenciaDTO;
import com.pablo.GESTIONASISTENCIA.dto.AsignacionDocenteDTO;
import com.pablo.GESTIONASISTENCIA.dto.PorcentajeAsistenciaDTO;
import com.pablo.GESTIONASISTENCIA.dto.UsuarioDTO;
import com.pablo.GESTIONASISTENCIA.model.Asistencia;
import com.pablo.GESTIONASISTENCIA.repository.AsistenciaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsistenciaService {

    @Autowired private AsistenciaRepository asistenciaRepository;
    @Autowired private RestTemplate restTemplate;

    public String registrarAsistencia(AsistenciaDTO dto) {
        UsuarioDTO alumno = obtenerUsuarioPorRut(dto.getRutAlumno());
        if (alumno == null) {
            return "No existe un usuario con RUT: " + dto.getRutAlumno();
        }
        AsignacionDocenteDTO asignacion = obtenerAsignacion(dto.getAsignacionDocenteId());
        if (asignacion == null) {
            return "Asignación docente no encontrada con ID: " + dto.getAsignacionDocenteId();
        }
        String estado = dto.getEstado();
        if (estado == null || (!estado.equals("PRESENTE") && !estado.equals("AUSENTE") && !estado.equals("ATRASADO"))) {
            return "Estado inválido. Use: PRESENTE, AUSENTE o ATRASADO.";
        }
        if (asistenciaRepository.existsByAlumnoIdAndAsignacionDocenteIdAndFecha(
                alumno.getId(), dto.getAsignacionDocenteId(), dto.getFecha())) {
            return "Ya existe asistencia registrada para este alumno en esta clase y fecha.";
        }
        asistenciaRepository.save(Asistencia.desde(dto, alumno.getId(), asignacion.getAsignaturaId()));
        return "Asistencia registrada exitosamente.";
    }

    // Devuelve asistencia enriquecida con nombreAsignatura — el front la usa para agrupar
    public Object obtenerAsistenciaPorAlumno(String rutAlumno) {
        UsuarioDTO alumno = obtenerUsuarioPorRut(rutAlumno);
        if (alumno == null) {
            return "No existe un usuario con RUT: " + rutAlumno;
        }
        List<Asistencia> registros = asistenciaRepository.findByAlumnoId(alumno.getId());
        List<AsistenciaDTO> resultado = new ArrayList<>();
        for (Asistencia asistencia : registros) {
            AsistenciaDTO asistenciaDTO = AsistenciaDTO.desde(asistencia);
            // Obtener nombre de asignatura desde GESTIONACADEMICO
            AsignacionDocenteDTO asignacion = obtenerAsignacion(asistencia.getAsignacionDocenteId());
            if (asignacion != null && asignacion.getNombreAsignatura() != null) {
                asistenciaDTO.setNombreAsignatura(asignacion.getNombreAsignatura());
            }
            resultado.add(asistenciaDTO);
        }
        return resultado;
    }

    public Object calcularPorcentaje(String rutAlumno) {
        UsuarioDTO alumno = obtenerUsuarioPorRut(rutAlumno);
        if (alumno == null) {
            return "No existe un usuario con RUT: " + rutAlumno;
        }
        List<Asistencia> registros = asistenciaRepository.findByAlumnoId(alumno.getId());
        PorcentajeAsistenciaDTO resultado = new PorcentajeAsistenciaDTO();
        resultado.setAlumnoId(alumno.getId());
        if (registros == null || registros.isEmpty()) {
            resultado.setTotalClases(0);
            resultado.setClasesPresente(0);
            resultado.setClasesAtrasado(0);
            resultado.setClasesAusente(0);
            resultado.setPorcentajeAsistencia(0.0);
            return resultado;
        }
        long totalClases = registros.size();
        long clasesPresente = registros.stream().filter(r -> "PRESENTE".equals(r.getEstado())).count();
        long clasesAtrasado = registros.stream().filter(r -> "ATRASADO".equals(r.getEstado())).count();
        long clasesAusente  = registros.stream().filter(r -> "AUSENTE".equals(r.getEstado())).count();
        resultado.setTotalClases(totalClases);
        resultado.setClasesPresente(clasesPresente);
        resultado.setClasesAtrasado(clasesAtrasado);
        resultado.setClasesAusente(clasesAusente);
        resultado.setPorcentajeAsistencia((clasesPresente * 100.0) / totalClases);
        return resultado;
    }

    public Object obtenerAsistenciaPorAsignacion(Long asignacionId) {
        List<Asistencia> registros = asistenciaRepository.findByAsignacionDocenteId(asignacionId);
        List<AsistenciaDTO> resultado = new ArrayList<>();
        for (Asistencia asistencia : registros) {
            resultado.add(AsistenciaDTO.desde(asistencia));
        }
        return resultado;
    }

    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackUsuarioPorRut")
    UsuarioDTO obtenerUsuarioPorRut(String rut) {
        return restTemplate.getForObject("http://GESTIONUSUARIO/usuarios/rut/" + rut, UsuarioDTO.class);
    }

    @CircuitBreaker(name = "academicoService", fallbackMethod = "fallbackAcademico")
    AsignacionDocenteDTO obtenerAsignacion(Long id) {
        return restTemplate.getForObject("http://GESTIONACADEMICO/asignaciones/" + id, AsignacionDocenteDTO.class);
    }

    UsuarioDTO fallbackUsuarioPorRut(String rut, Throwable t) { return null; }
    AsignacionDocenteDTO fallbackAcademico(Long id, Throwable t) { return null; }
}
