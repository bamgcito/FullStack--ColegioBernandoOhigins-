package com.pablo.GESTIONCURSO.dto;

import com.pablo.GESTIONCURSO.model.CursoAlumno;

public class CursoAlumnoDTO {

    private Long cursoId;
    private Long alumnoId;

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public static CursoAlumnoDTO desde(CursoAlumno ca) {
        CursoAlumnoDTO dto = new CursoAlumnoDTO();
        dto.setCursoId(ca.getCurso().getId());
        dto.setAlumnoId(ca.getAlumnoId());
        return dto;
    }
}
