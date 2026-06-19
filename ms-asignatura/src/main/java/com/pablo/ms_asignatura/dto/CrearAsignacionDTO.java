package com.pablo.ms_asignatura.dto;

public class CrearAsignacionDTO {

    private Long asignaturaId;
    private String rutProfesor;
    private Long cursoId;

    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }

    public String getRutProfesor() { return rutProfesor; }
    public void setRutProfesor(String rutProfesor) { this.rutProfesor = rutProfesor; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
}
