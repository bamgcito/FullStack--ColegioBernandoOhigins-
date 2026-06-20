package com.pablo.ms_horarios.dto;

import com.pablo.ms_horarios.model.Horario;
import java.time.LocalTime;

public class HorarioDTO {

    private Long id;
    private Long asignacionDocenteId;
    private Long profesorId;
    private Long cursoId;
    private String diaSemana;
    private Integer bloque;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String nombreAsignatura;
    private String nombreCurso;

    public static HorarioDTO desde(Horario h) {
        HorarioDTO dto = new HorarioDTO();
        dto.setId(h.getId());
        dto.setAsignacionDocenteId(h.getAsignacionDocenteId());
        dto.setProfesorId(h.getProfesorId());
        dto.setCursoId(h.getCursoId());
        dto.setDiaSemana(h.getDiaSemana());
        dto.setBloque(h.getBloque());
        dto.setHoraInicio(h.getHoraInicio());
        dto.setHoraFin(h.getHoraFin());
        return dto;
    }

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

    public String getNombreAsignatura() { return nombreAsignatura; }
    public void setNombreAsignatura(String nombreAsignatura) { this.nombreAsignatura = nombreAsignatura; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }
}
