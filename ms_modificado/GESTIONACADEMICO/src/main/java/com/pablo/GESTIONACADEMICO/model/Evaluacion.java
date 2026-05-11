package com.pablo.GESTIONACADEMICO.model;

import com.pablo.GESTIONACADEMICO.dto.EvaluacionDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "evaluaciones")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asignacion_docente_id")
    private AsignacionDocente asignacionDocente;

    public Evaluacion() {}

    public static Evaluacion desde(EvaluacionDTO dto, AsignacionDocente asignacion) {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setNombre(dto.getNombre());
        evaluacion.setFecha(dto.getFecha());
        evaluacion.setAsignacionDocente(asignacion);
        return evaluacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public AsignacionDocente getAsignacionDocente() { return asignacionDocente; }
    public void setAsignacionDocente(AsignacionDocente asignacionDocente) { this.asignacionDocente = asignacionDocente; }
}
