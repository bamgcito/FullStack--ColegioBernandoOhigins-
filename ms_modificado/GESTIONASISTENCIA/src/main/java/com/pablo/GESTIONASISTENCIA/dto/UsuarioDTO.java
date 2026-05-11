package com.pablo.GESTIONASISTENCIA.dto;

public class UsuarioDTO {

    private Long id;
    private String rut;
    private String nombreRol;
    private String fechaCreacion;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String rut, String nombreRol, String fechaCreacion) {
        this.id = id;
        this.rut = rut;
        this.nombreRol = nombreRol;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
