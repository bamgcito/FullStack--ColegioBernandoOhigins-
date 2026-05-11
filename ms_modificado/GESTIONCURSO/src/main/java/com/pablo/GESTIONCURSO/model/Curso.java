package com.pablo.GESTIONCURSO.model;

import com.pablo.GESTIONCURSO.dto.CursoDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nivel")
    private String nivel;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "letra")
    private String letra;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "profesor_jefe_id")
    private Long profesorJefeId;

    public Curso() {}

    public static Curso desde(CursoDTO dto) {
        Curso curso = new Curso();
        curso.setNivel(dto.getNivel());
        curso.setTipo(dto.getTipo());
        curso.setLetra(dto.getLetra());
        curso.setAnio(dto.getAnio());
        return curso;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getLetra() { return letra; }
    public void setLetra(String letra) { this.letra = letra; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Long getProfesorJefeId() { return profesorJefeId; }
    public void setProfesorJefeId(Long profesorJefeId) { this.profesorJefeId = profesorJefeId; }
}
