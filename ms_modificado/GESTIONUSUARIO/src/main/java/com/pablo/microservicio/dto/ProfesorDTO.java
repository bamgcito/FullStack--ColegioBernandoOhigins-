package com.pablo.microservicio.dto;

import java.time.LocalDate;

import com.pablo.microservicio.model.Profesor;
import com.pablo.microservicio.model.Usuario;

public class ProfesorDTO {

    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String especialidad;
    private String telefono;
    private String correoInstitucional;
    private LocalDate fechaContrato;

    public ProfesorDTO() {}

    public static ProfesorDTO desde(Profesor p, Usuario u) {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(p.getId());
        dto.setRut(u.getRut());
        dto.setNombre(p.getNombre());
        dto.setApellido(p.getApellido());
        dto.setFechaNacimiento(p.getFechaNacimiento());
        dto.setEdad(p.getEdad());
        dto.setEspecialidad(p.getEspecialidad());
        dto.setTelefono(p.getTelefono());
        dto.setCorreoInstitucional(p.getCorreoInstitucional());
        dto.setFechaContrato(p.getFechaContrato());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) { this.correoInstitucional = correoInstitucional; }

    public LocalDate getFechaContrato() { return fechaContrato; }
    public void setFechaContrato(LocalDate fechaContrato) { this.fechaContrato = fechaContrato; }
}
