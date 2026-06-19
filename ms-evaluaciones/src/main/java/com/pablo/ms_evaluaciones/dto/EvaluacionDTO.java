package com.pablo.ms_evaluaciones.dto;

import com.pablo.ms_evaluaciones.model.Evaluacion;
import java.time.LocalDateTime;

public class EvaluacionDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private Long asignacionId;
    private LocalDateTime fechaCreacion;

    public static EvaluacionDTO desde(Evaluacion e) {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setId(e.getId());
        dto.setTitulo(e.getTitulo());
        dto.setDescripcion(e.getDescripcion());
        dto.setAsignacionId(e.getAsignacionId());
        dto.setFechaCreacion(e.getFechaCreacion());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Long getAsignacionId() { return asignacionId; }
    public void setAsignacionId(Long asignacionId) { this.asignacionId = asignacionId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
