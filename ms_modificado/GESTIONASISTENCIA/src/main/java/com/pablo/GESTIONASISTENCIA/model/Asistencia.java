package com.pablo.GESTIONASISTENCIA.model;

import com.pablo.GESTIONASISTENCIA.dto.AsistenciaDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "asistencia")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long alumnoId;
    private Long asignacionDocenteId;
    private Long asignaturaId;
    private String fecha;
    private String estado;

    public Asistencia() {}

    public static Asistencia desde(AsistenciaDTO dto, Long alumnoId, Long asignaturaId) {
        Asistencia entidad = new Asistencia();
        entidad.setAlumnoId(alumnoId);
        entidad.setAsignacionDocenteId(dto.getAsignacionDocenteId());
        entidad.setAsignaturaId(asignaturaId); // ← viene del parámetro
        entidad.setFecha(dto.getFecha());
        entidad.setEstado(dto.getEstado());
        return entidad;
    }

    @PrePersist
    public void asignarFecha() {
        if (fecha == null) {
            fecha = LocalDate.now().toString();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public Long getAsignacionDocenteId() { return asignacionDocenteId; }
    public void setAsignacionDocenteId(Long asignacionDocenteId) { this.asignacionDocenteId = asignacionDocenteId; }

    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
