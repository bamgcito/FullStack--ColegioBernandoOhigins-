package com.pablo.GESTIONACADEMICO.dto;

public class PromedioAsignaturaDTO {
    
    private String nombreAsignatura;
    private Double promedio;

    public PromedioAsignaturaDTO() {}

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    
}
