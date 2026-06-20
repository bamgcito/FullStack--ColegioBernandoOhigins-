package com.pablo.ms_horarios.dto;

import java.time.LocalTime;

public class CrearHorarioDTO {

    private Long asignacionDocenteId;
    private String diaSemana;
    private Integer bloque;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public Long getAsignacionDocenteId() { return asignacionDocenteId; }
    public void setAsignacionDocenteId(Long asignacionDocenteId) { this.asignacionDocenteId = asignacionDocenteId; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public Integer getBloque() { return bloque; }
    public void setBloque(Integer bloque) { this.bloque = bloque; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
}
