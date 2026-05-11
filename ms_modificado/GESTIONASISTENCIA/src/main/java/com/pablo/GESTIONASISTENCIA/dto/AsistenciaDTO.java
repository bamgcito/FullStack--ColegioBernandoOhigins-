package com.pablo.GESTIONASISTENCIA.dto;

import com.pablo.GESTIONASISTENCIA.model.Asistencia;

public class AsistenciaDTO {

    private Long id;
    private String rutAlumno;
    private String nombreAlumno;     // el front usa a.nombre en asistencia agrupada
    private String apellidoAlumno;
    private Long asignacionDocenteId;
    private String nombreAsignatura; // el front usa a.nombre para el grupo de asignatura
    private String fecha;
    private String estado;

    public AsistenciaDTO() {}

    public static AsistenciaDTO desde(Asistencia entidad) {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setId(entidad.getId());
        dto.setAsignacionDocenteId(entidad.getAsignacionDocenteId());
        dto.setFecha(entidad.getFecha());
        dto.setEstado(entidad.getEstado());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRutAlumno() { return rutAlumno; }
    public void setRutAlumno(String rutAlumno) { this.rutAlumno = rutAlumno; }

    public String getNombreAlumno() { return nombreAlumno; }
    public void setNombreAlumno(String nombreAlumno) { this.nombreAlumno = nombreAlumno; }

    public String getApellidoAlumno() { return apellidoAlumno; }
    public void setApellidoAlumno(String apellidoAlumno) { this.apellidoAlumno = apellidoAlumno; }

    public Long getAsignacionDocenteId() { return asignacionDocenteId; }
    public void setAsignacionDocenteId(Long asignacionDocenteId) { this.asignacionDocenteId = asignacionDocenteId; }

    public String getNombreAsignatura() { return nombreAsignatura; }
    public void setNombreAsignatura(String nombreAsignatura) { this.nombreAsignatura = nombreAsignatura; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
