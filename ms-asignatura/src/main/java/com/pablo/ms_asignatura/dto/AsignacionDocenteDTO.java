package com.pablo.ms_asignatura.dto;

import com.pablo.ms_asignatura.model.AsignacionDocente;
import java.time.LocalDateTime;

public class AsignacionDocenteDTO {

    private Long id;
    private Long asignaturaId;
    private String nombreAsignatura;
    private Long profesorId;
    private String nombreProfesor;
    private Long cursoId;
    private String nombreCurso;
    private LocalDateTime fechaCreacion;

    public static AsignacionDocenteDTO desde(AsignacionDocente a) {
        AsignacionDocenteDTO dto = new AsignacionDocenteDTO();
        dto.setId(a.getId());
        dto.setAsignaturaId(a.getAsignatura().getId());
        dto.setNombreAsignatura(a.getAsignatura().getNombre());
        dto.setProfesorId(a.getProfesorId());
        dto.setCursoId(a.getCursoId());
        dto.setFechaCreacion(a.getFechaCreacion());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }

    public String getNombreAsignatura() { return nombreAsignatura; }
    public void setNombreAsignatura(String nombreAsignatura) { this.nombreAsignatura = nombreAsignatura; }

    public Long getProfesorId() { return profesorId; }
    public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }

    public String getNombreProfesor() { return nombreProfesor; }
    public void setNombreProfesor(String nombreProfesor) { this.nombreProfesor = nombreProfesor; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
