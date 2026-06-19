package com.pablo.ms_perfiles.dto;

import com.pablo.ms_perfiles.model.Alumno;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlumnoDTO {

    private Long id;
    private Long usuarioId;
    private String rut;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaCreacion;

    public AlumnoDTO() {}

    public static AlumnoDTO desde(Alumno a) {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setId(a.getId());
        dto.setUsuarioId(a.getUsuarioId());
        dto.setRut(a.getRut());
        dto.setNombre(a.getNombre());
        dto.setApellido(a.getApellido());
        dto.setFechaNacimiento(a.getFechaNacimiento());
        dto.setFechaCreacion(a.getFechaCreacion());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
