package com.pablo.microservicio.dto;

public class AsociarApoderadoDTO {

    private String rutAlumno;
    private String rutApoderado;
    private Boolean esPrincipal;

    public AsociarApoderadoDTO() {}

    public String getRutAlumno() { return rutAlumno; }
    public void setRutAlumno(String rutAlumno) { this.rutAlumno = rutAlumno; }

    public String getRutApoderado() { return rutApoderado; }
    public void setRutApoderado(String rutApoderado) { this.rutApoderado = rutApoderado; }

    public Boolean getEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(Boolean esPrincipal) { this.esPrincipal = esPrincipal; }
}
