package com.pablo.GESTIONACADEMICO.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pablo.GESTIONACADEMICO.model.Nota;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotaDTO {

    private Long id;
    private String rutAlumno;
    private String nombre;
    private String apellido;
    private Long evaluacionId;
    private String evalNombre;      // nombre de la evaluación — el front lo usa como n.evalNombre
    private String fechaEvaluacion; // fecha de la evaluación — el front lo usa como n.fecha
    private String nombreAsignatura;// nombre de la asignatura — el front lo usa como a.nombre
    private Double nota;

    public static NotaDTO desde(Nota n) {
        NotaDTO dto = new NotaDTO();
        dto.setId(n.getId());
        dto.setEvaluacionId(n.getEvaluacion().getId());
        dto.setEvalNombre(n.getEvaluacion().getNombre());
        dto.setFechaEvaluacion(n.getEvaluacion().getFecha());
        dto.setNombreAsignatura(n.getEvaluacion().getAsignacionDocente().getAsignatura().getNombre());
        dto.setNota(n.getNota());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRutAlumno() { return rutAlumno; }
    public void setRutAlumno(String rutAlumno) { this.rutAlumno = rutAlumno; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public Long getEvaluacionId() { return evaluacionId; }
    public void setEvaluacionId(Long evaluacionId) { this.evaluacionId = evaluacionId; }

    public String getEvalNombre() { return evalNombre; }
    public void setEvalNombre(String evalNombre) { this.evalNombre = evalNombre; }

    public String getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(String fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }

    public String getNombreAsignatura() { return nombreAsignatura; }
    public void setNombreAsignatura(String nombreAsignatura) { this.nombreAsignatura = nombreAsignatura; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }
}
