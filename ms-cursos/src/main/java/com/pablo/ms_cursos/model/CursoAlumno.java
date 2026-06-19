package com.pablo.ms_cursos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "curso_alumno")
public class CursoAlumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "alumno_id", nullable = false)
    private Long alumnoId;

    public CursoAlumno() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }
}
