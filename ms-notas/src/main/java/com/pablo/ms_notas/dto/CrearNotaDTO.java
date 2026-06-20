package com.pablo.ms_notas.dto;

public class CrearNotaDTO {

    private Long evaluacionId;
    private Long alumnoId;
    private Double valor;

    public Long getEvaluacionId() { return evaluacionId; }
    public void setEvaluacionId(Long evaluacionId) { this.evaluacionId = evaluacionId; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}
