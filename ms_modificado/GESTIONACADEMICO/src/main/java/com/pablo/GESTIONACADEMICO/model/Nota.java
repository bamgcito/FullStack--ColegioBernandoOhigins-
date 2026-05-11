package com.pablo.GESTIONACADEMICO.model;

import com.pablo.GESTIONACADEMICO.dto.NotaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "notas")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alumno_id")
    private Long alumnoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evaluacion_id")
    private Evaluacion evaluacion;

    private Double nota;

    @Column(name = "fecha_registro")
    private String fechaRegistro;

    public Nota() {}

    public static Nota desde(NotaDTO dto, Evaluacion evaluacion, Long alumnoId) {
        Nota nota = new Nota();
        nota.setAlumnoId(alumnoId);
        nota.setEvaluacion(evaluacion);
        nota.setNota(dto.getNota());
        return nota;
    }

    @PrePersist
    public void asignarFechaRegistro() {
        this.fechaRegistro = LocalDate.now().toString();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public Evaluacion getEvaluacion() { return evaluacion; }
    public void setEvaluacion(Evaluacion evaluacion) { this.evaluacion = evaluacion; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
