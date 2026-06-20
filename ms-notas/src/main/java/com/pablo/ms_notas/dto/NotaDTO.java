package com.pablo.ms_notas.dto;

import com.pablo.ms_notas.model.Nota;
import java.time.LocalDateTime;

public class NotaDTO {

    private Long id;
    private Long evaluacionId;
    private String tituloEvaluacion;
    private Long asignacionId;
    private String nombreAsignatura;
    private Long alumnoId;
    private Double valor;
    private LocalDateTime fechaCreacion;

    public static NotaDTO desde(Nota n) {
        NotaDTO dto = new NotaDTO();
        dto.setId(n.getId());
        dto.setEvaluacionId(n.getEvaluacionId());
        dto.setAlumnoId(n.getAlumnoId());
        dto.setValor(n.getValor());
        dto.setFechaCreacion(n.getFechaCreacion());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEvaluacionId() { return evaluacionId; }
    public void setEvaluacionId(Long evaluacionId) { this.evaluacionId = evaluacionId; }

    public String getTituloEvaluacion() { return tituloEvaluacion; }
    public void setTituloEvaluacion(String tituloEvaluacion) { this.tituloEvaluacion = tituloEvaluacion; }

    public Long getAsignacionId() { return asignacionId; }
    public void setAsignacionId(Long asignacionId) { this.asignacionId = asignacionId; }

    public String getNombreAsignatura() { return nombreAsignatura; }
    public void setNombreAsignatura(String nombreAsignatura) { this.nombreAsignatura = nombreAsignatura; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
