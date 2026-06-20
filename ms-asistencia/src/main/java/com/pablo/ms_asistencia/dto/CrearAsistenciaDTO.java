package com.pablo.ms_asistencia.dto;

import com.pablo.ms_asistencia.model.EstadoAsistencia;
import java.time.LocalDate;

public class CrearAsistenciaDTO {

    private Long alumnoId;
    private Long asignacionId;
    private EstadoAsistencia estado;
    private LocalDate fecha;

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public Long getAsignacionId() { return asignacionId; }
    public void setAsignacionId(Long asignacionId) { this.asignacionId = asignacionId; }

    public EstadoAsistencia getEstado() { return estado; }
    public void setEstado(EstadoAsistencia estado) { this.estado = estado; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
