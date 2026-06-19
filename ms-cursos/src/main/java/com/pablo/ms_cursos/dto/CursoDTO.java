package com.pablo.ms_cursos.dto;

import com.pablo.ms_cursos.model.Curso;
import java.time.LocalDateTime;

public class CursoDTO {

    private Long id;
    private String nombre;
    private String nivel;
    private String letra;
    private Integer anio;
    private Long profesorJefeId;
    private String nombreProfesorJefe;
    private LocalDateTime fechaCreacion;
    private int alumnosCount;

    public CursoDTO() {}

    public static CursoDTO desde(Curso c) {
        CursoDTO dto = new CursoDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setNivel(c.getNivel());
        dto.setLetra(c.getLetra());
        dto.setAnio(c.getAnio());
        dto.setProfesorJefeId(c.getProfesorJefeId());
        dto.setFechaCreacion(c.getFechaCreacion());
        return dto;
    }

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

    public String getNombreProfesorJefe() { return nombreProfesorJefe; }
    public void setNombreProfesorJefe(String nombreProfesorJefe) { this.nombreProfesorJefe = nombreProfesorJefe; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public int getAlumnosCount() { return alumnosCount; }
    public void setAlumnosCount(int alumnosCount) { this.alumnosCount = alumnosCount; }
}
