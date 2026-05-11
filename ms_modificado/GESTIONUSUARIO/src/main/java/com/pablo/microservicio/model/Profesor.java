package com.pablo.microservicio.model;

import com.pablo.microservicio.dto.ProfesorDTO;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "profesores")
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column
    private Integer edad;

    @Column(length = 100)
    private String especialidad;

    @Column(length = 20)
    private String telefono;

    @Column(name = "correo_institucional", length = 150)
    private String correoInstitucional;

    @Column(name = "fecha_contrato")
    private LocalDate fechaContrato;

    public Profesor() {}

    public static Profesor desde(ProfesorDTO dto, Usuario usuario) {
        Profesor profesor = new Profesor();
        profesor.setUsuario(usuario);
        profesor.setNombre(dto.getNombre());
        profesor.setApellido(dto.getApellido());
        profesor.setFechaNacimiento(dto.getFechaNacimiento());
        profesor.setEdad(dto.getEdad());
        profesor.setEspecialidad(dto.getEspecialidad());
        profesor.setTelefono(dto.getTelefono());
        profesor.setCorreoInstitucional(dto.getCorreoInstitucional());
        profesor.setFechaContrato(dto.getFechaContrato());
        return profesor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

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
