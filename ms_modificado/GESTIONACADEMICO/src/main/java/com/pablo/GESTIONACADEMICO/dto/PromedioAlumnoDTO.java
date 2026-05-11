package com.pablo.GESTIONACADEMICO.dto;

import java.util.List;

public class PromedioAlumnoDTO {

    private String rutAlumno;
    private Double promedioGeneral;
    private List<PromedioAsignaturaDTO> promediosPorAsignatura;

    public PromedioAlumnoDTO() {}

    public String getRutAlumno() {
        return rutAlumno;
    }

    public void setRutAlumno(String rutAlumno) {
        this.rutAlumno = rutAlumno;
    }

    public Double getPromedioGeneral() {
        return promedioGeneral;
    }

    public void setPromedioGeneral(Double promedioGeneral) {
        this.promedioGeneral = promedioGeneral;
    }

    public List<PromedioAsignaturaDTO> getPromediosPorAsignatura() {
        return promediosPorAsignatura;
    }

    public void setPromediosPorAsignatura(List<PromedioAsignaturaDTO> promediosPorAsignatura) {
        this.promediosPorAsignatura = promediosPorAsignatura;
    }

    
}
