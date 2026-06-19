package com.pablo.ms_cursos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String nivel;

    @Column(nullable = false, length = 5)
    private String letra;

    @Column(nullable = false)
    private Integer anio;

    @Column(name = "profesor_jefe_id")
    private Long profesorJefeId;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void alCrear() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Curso() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getLetra() { return letra; }
    public void setLetra(String letra) { this.letra = letra; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Long getProfesorJefeId() { return profesorJefeId; }
    public void setProfesorJefeId(Long profesorJefeId) { this.profesorJefeId = profesorJefeId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
