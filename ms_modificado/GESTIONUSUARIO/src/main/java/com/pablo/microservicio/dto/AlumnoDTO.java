package com.pablo.microservicio.dto;

import com.pablo.microservicio.model.Alumno;
import java.time.LocalDate;

public class AlumnoDTO {

    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String direccion;
    private LocalDate fechaMatricula;

    public AlumnoDTO() {}

    public static AlumnoDTO desde(Alumno a) {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setId(a.getId());
        dto.setRut(a.getUsuario().getRut());
        dto.setNombre(a.getNombre());
        dto.setApellido(a.getApellido());
        dto.setFechaNacimiento(a.getFechaNacimiento());
        dto.setEdad(a.getEdad());
        dto.setDireccion(a.getDireccion());
        dto.setFechaMatricula(a.getFechaMatricula());
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

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDate getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(LocalDate fechaMatricula) { this.fechaMatricula = fechaMatricula; }
}
