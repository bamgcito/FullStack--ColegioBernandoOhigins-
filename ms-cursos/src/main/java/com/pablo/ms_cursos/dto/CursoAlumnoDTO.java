package com.pablo.ms_cursos.dto;

import com.pablo.ms_cursos.model.CursoAlumno;

public class CursoAlumnoDTO {

    private Long id;
    private Long cursoId;
    private Long alumnoId;
    private String nombre;
    private String apellido;
    private String rut;

    public CursoAlumnoDTO() {}

    public static CursoAlumnoDTO desde(CursoAlumno ca) {
        CursoAlumnoDTO dto = new CursoAlumnoDTO();
        dto.setId(ca.getId());
        dto.setCursoId(ca.getCurso().getId());
        dto.setAlumnoId(ca.getAlumnoId());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
}
