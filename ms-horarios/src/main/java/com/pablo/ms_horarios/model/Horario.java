package com.pablo.ms_horarios.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "horarios")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long asignacionDocenteId;

    @Column(nullable = false)
    private Long profesorId;

    @Column(nullable = false)
    private Long cursoId;

    @Column(nullable = false, length = 20)
    private String diaSemana;

    @Column(nullable = false)
    private Integer bloque;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAsignacionDocenteId() { return asignacionDocenteId; }
    public void setAsignacionDocenteId(Long asignacionDocenteId) { this.asignacionDocenteId = asignacionDocenteId; }

    public Long getProfesorId() { return profesorId; }
    public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public Integer getBloque() { return bloque; }
    public void setBloque(Integer bloque) { this.bloque = bloque; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
}
