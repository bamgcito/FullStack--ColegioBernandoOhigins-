package com.pablo.ms_asistencia.dto;

import com.pablo.ms_asistencia.model.Asistencia;
import com.pablo.ms_asistencia.model.EstadoAsistencia;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AsistenciaDTO {

    private Long id;
    private Long alumnoId;
    private Long asignacionId;
    private EstadoAsistencia estado;
    private LocalDate fecha;
    private LocalDateTime fechaCreacion;

    public static AsistenciaDTO desde(Asistencia a) {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setId(a.getId());
        dto.setAlumnoId(a.getAlumnoId());
        dto.setAsignacionId(a.getAsignacionId());
        dto.setEstado(a.getEstado());
        dto.setFecha(a.getFecha());
        dto.setFechaCreacion(a.getFechaCreacion());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public Long getAsignacionId() { return asignacionId; }
    public void setAsignacionId(Long asignacionId) { this.asignacionId = asignacionId; }

    public EstadoAsistencia getEstado() { return estado; }
    public void setEstado(EstadoAsistencia estado) { this.estado = estado; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
