package com.pablo.GESTIONCURSO.dto;

public class AsignarAlumnoDTO {

    private Long cursoId;
    private String alumnoRut;

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public String getAlumnoRut() {
        return alumnoRut;
    }

    public void setAlumnoRut(String alumnoRut) {
        this.alumnoRut = alumnoRut;
    }
}
