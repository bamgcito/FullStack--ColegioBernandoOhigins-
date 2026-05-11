package com.pablo.GESTIONACADEMICO.model;

import com.pablo.GESTIONACADEMICO.dto.AsignacionDocenteDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "asignacion_docente")
public class AsignacionDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "curso_id")
    private Long cursoId;

    @Column(name = "profesor_id")
    private Long profesorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asignatura_id")
    private Asignatura asignatura;

    private String fecha;

    private String estado;

    public AsignacionDocente() {}

    public static AsignacionDocente desde(AsignacionDocenteDTO dto, Asignatura asignatura, Long profesorId) {
        AsignacionDocente asignacion = new AsignacionDocente();
        asignacion.setCursoId(dto.getCursoId());
        asignacion.setProfesorId(profesorId);
        asignacion.setAsignatura(asignatura);
        asignacion.setFecha(dto.getFecha());
        asignacion.setEstado(dto.getEstado());
        return asignacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Long getProfesorId() { return profesorId; }
    public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }

    public Asignatura getAsignatura() { return asignatura; }
    public void setAsignatura(Asignatura asignatura) { this.asignatura = asignatura; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
