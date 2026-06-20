package com.pablo.ms_asistencia.dto;

import com.pablo.ms_asistencia.model.Anotacion;
import com.pablo.ms_asistencia.model.TipoAnotacion;
import java.time.LocalDateTime;

public class AnotacionDTO {

    private Long id;
    private Long alumnoId;
    private Long asignacionId;
    private TipoAnotacion tipo;
    private String descripcion;
    private LocalDateTime fechaCreacion;

    public static AnotacionDTO desde(Anotacion a) {
        AnotacionDTO dto = new AnotacionDTO();
        dto.setId(a.getId());
        dto.setAlumnoId(a.getAlumnoId());
        dto.setAsignacionId(a.getAsignacionId());
        dto.setTipo(a.getTipo());
        dto.setDescripcion(a.getDescripcion());
        dto.setFechaCreacion(a.getFechaCreacion());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public Long getAsignacionId() { return asignacionId; }
    public void setAsignacionId(Long asignacionId) { this.asignacionId = asignacionId; }

    public TipoAnotacion getTipo() { return tipo; }
    public void setTipo(TipoAnotacion tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
