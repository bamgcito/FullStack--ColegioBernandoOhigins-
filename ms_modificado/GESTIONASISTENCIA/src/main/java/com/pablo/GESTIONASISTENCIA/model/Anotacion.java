package com.pablo.GESTIONASISTENCIA.model;

import com.pablo.GESTIONASISTENCIA.dto.AnotacionDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "anotaciones")
public class Anotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long alumnoId;
    private Long asignacionDocenteId;
    private String descripcion;
    private String tipo;
    private String fecha;

    public Anotacion() {}

    public static Anotacion desde(AnotacionDTO dto, Long alumnoId) {
        Anotacion entidad = new Anotacion();
        entidad.setAlumnoId(alumnoId);
        entidad.setAsignacionDocenteId(dto.getAsignacionDocenteId());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setTipo(dto.getTipo());
        entidad.setFecha(dto.getFecha());
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

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
