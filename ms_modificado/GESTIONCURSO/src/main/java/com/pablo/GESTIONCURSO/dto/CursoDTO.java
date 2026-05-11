package com.pablo.GESTIONCURSO.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pablo.GESTIONCURSO.model.Curso;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CursoDTO {

    private Long id;
    private String nivel;
    private String tipo;
    private String letra;
    private Integer anio;
    @JsonIgnore
    private Long profesorJefeId;
    private String nombreProfesorJefe;
    private List<AlumnoDetalleDTO> alumnos;

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

    public String getNombreProfesorJefe() { return nombreProfesorJefe; }
    public void setNombreProfesorJefe(String n) { this.nombreProfesorJefe = n; }

    public List<AlumnoDetalleDTO> getAlumnos() { return alumnos; }
    public void setAlumnos(List<AlumnoDetalleDTO> alumnos) { this.alumnos = alumnos; }

    public static CursoDTO desde(Curso c) {
        CursoDTO dto = new CursoDTO();
        dto.setId(c.getId());
        dto.setNivel(c.getNivel());
        dto.setTipo(c.getTipo());
        dto.setLetra(c.getLetra());
        dto.setAnio(c.getAnio());
        return dto;
    }
}
