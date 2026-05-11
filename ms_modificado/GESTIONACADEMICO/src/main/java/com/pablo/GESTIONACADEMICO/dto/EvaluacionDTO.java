package com.pablo.GESTIONACADEMICO.dto;

import com.pablo.GESTIONACADEMICO.model.Evaluacion;

public class EvaluacionDTO {

    private Long id;
    private String nombre;
    private String fecha;
    private Long asignacionDocenteId;

    public EvaluacionDTO() {}

    public EvaluacionDTO(Long id, String nombre, String fecha, Long asignacionDocenteId) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.asignacionDocenteId = asignacionDocenteId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public Long getAsignacionDocenteId() { return asignacionDocenteId; }
    public void setAsignacionDocenteId(Long asignacionDocenteId) { this.asignacionDocenteId = asignacionDocenteId; }

    public static EvaluacionDTO desde(Evaluacion e) {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setFecha(e.getFecha());
        dto.setAsignacionDocenteId(e.getAsignacionDocente().getId());
        return dto;
    }
}
