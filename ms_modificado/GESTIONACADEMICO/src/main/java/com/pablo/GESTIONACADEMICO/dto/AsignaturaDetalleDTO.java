package com.pablo.GESTIONACADEMICO.dto;

import com.pablo.GESTIONACADEMICO.model.Asignatura;
import com.pablo.GESTIONACADEMICO.model.AsignacionDocente;

import java.util.List;

public class AsignaturaDetalleDTO {

    private Long asignaturaId;
    private String nombreAsignatura;
    private Long asignacionId;
    private Long cursoId;
    private String nombreCurso;
    private Long profesorId;
    private String nombreProfesor;
    private String apellidoProfesor;
    private String estado;
    private List<EvaluacionDTO> evaluaciones;

    public AsignaturaDetalleDTO() {}

    public static AsignaturaDetalleDTO desde(Asignatura asignatura, AsignacionDocente asignacion,
                                              CursoDTO curso, ProfesorDTO profesor,
                                              List<EvaluacionDTO> evaluaciones) {
        AsignaturaDetalleDTO dto = new AsignaturaDetalleDTO();
        dto.setAsignaturaId(asignatura.getId());
        dto.setNombreAsignatura(asignatura.getNombre());
        dto.setAsignacionId(asignacion.getId());
        dto.setCursoId(asignacion.getCursoId());
        dto.setEstado(asignacion.getEstado());
        dto.setEvaluaciones(evaluaciones);

        if (curso != null) {
            dto.setNombreCurso(curso.getNivel() + "° " + curso.getTipo() + " " + curso.getLetra());
        }
        if (profesor != null) {
            dto.setProfesorId(asignacion.getProfesorId());
            dto.setNombreProfesor(profesor.getNombre());
            dto.setApellidoProfesor(profesor.getApellido());
        }
        return dto;
    }

    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }

    public String getNombreAsignatura() { return nombreAsignatura; }
    public void setNombreAsignatura(String nombreAsignatura) { this.nombreAsignatura = nombreAsignatura; }

    public Long getAsignacionId() { return asignacionId; }
    public void setAsignacionId(Long asignacionId) { this.asignacionId = asignacionId; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }

    public Long getProfesorId() { return profesorId; }
    public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }

    public String getNombreProfesor() { return nombreProfesor; }
    public void setNombreProfesor(String nombreProfesor) { this.nombreProfesor = nombreProfesor; }

    public String getApellidoProfesor() { return apellidoProfesor; }
    public void setApellidoProfesor(String apellidoProfesor) { this.apellidoProfesor = apellidoProfesor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<EvaluacionDTO> getEvaluaciones() { return evaluaciones; }
    public void setEvaluaciones(List<EvaluacionDTO> evaluaciones) { this.evaluaciones = evaluaciones; }
}
