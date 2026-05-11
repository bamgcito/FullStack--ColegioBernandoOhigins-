package com.pablo.GESTIONASISTENCIA.dto;

import com.pablo.GESTIONASISTENCIA.model.Anotacion;

public class AnotacionDTO {

    private Long id;
    private String rutAlumno;
    private Long asignacionDocenteId;
    private String descripcion;
    private String tipo;
    private String fecha;

    public AnotacionDTO() {}

    public static AnotacionDTO desde(Anotacion entidad) {
        AnotacionDTO dto = new AnotacionDTO();
        dto.setId(entidad.getId());
        dto.setAsignacionDocenteId(entidad.getAsignacionDocenteId());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setTipo(entidad.getTipo());
        dto.setFecha(entidad.getFecha());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRutAlumno() { return rutAlumno; }
    public void setRutAlumno(String rutAlumno) { this.rutAlumno = rutAlumno; }

    public Long getAsignacionDocenteId() { return asignacionDocenteId; }
    public void setAsignacionDocenteId(Long asignacionDocenteId) { this.asignacionDocenteId = asignacionDocenteId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
