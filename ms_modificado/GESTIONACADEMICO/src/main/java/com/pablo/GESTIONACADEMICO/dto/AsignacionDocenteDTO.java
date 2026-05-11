package com.pablo.GESTIONACADEMICO.dto;

import com.pablo.GESTIONACADEMICO.model.AsignacionDocente;

public class AsignacionDocenteDTO {

    private Long id;
    private Long cursoId;
    private String rutProfesor;
    private Long profesorId;
    private Long asignaturaId;
    private String nombreAsignatura; // ← agregado para que GESTIONASISTENCIA pueda enriquecer sus respuestas
    private String fecha;
    private String estado;

    public AsignacionDocenteDTO() {}

    public static AsignacionDocenteDTO desde(AsignacionDocente a) {
        AsignacionDocenteDTO dto = new AsignacionDocenteDTO();
        dto.setId(a.getId());
        dto.setCursoId(a.getCursoId());
        dto.setProfesorId(a.getProfesorId());
        dto.setAsignaturaId(a.getAsignatura().getId());
        dto.setNombreAsignatura(a.getAsignatura().getNombre()); // ← incluir nombre
        dto.setFecha(a.getFecha());
        dto.setEstado(a.getEstado());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getRutProfesor() { return rutProfesor; }
    public void setRutProfesor(String rutProfesor) { this.rutProfesor = rutProfesor; }

    public Long getProfesorId() { return profesorId; }
    public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }

    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }

    public String getNombreAsignatura() { return nombreAsignatura; }
    public void setNombreAsignatura(String nombreAsignatura) { this.nombreAsignatura = nombreAsignatura; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
