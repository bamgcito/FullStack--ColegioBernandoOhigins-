package com.pablo.ms_asistencia.dto;

import com.pablo.ms_asistencia.model.TipoAnotacion;

public class CrearAnotacionDTO {

    private Long alumnoId;
    private Long asignacionId;
    private TipoAnotacion tipo;
    private String descripcion;

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public Long getAsignacionId() { return asignacionId; }
    public void setAsignacionId(Long asignacionId) { this.asignacionId = asignacionId; }

    public TipoAnotacion getTipo() { return tipo; }
    public void setTipo(TipoAnotacion tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
