package com.pablo.microservicio.model;

import com.pablo.microservicio.dto.ApoderadoDTO;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "apoderados")
public class Apoderado {

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

    @Column(length = 20)
    private String telefono;

    @Column(name = "telefono_emergencia", length = 20)
    private String telefonoEmergencia;

    @Column(length = 150)
    private String correo;

    @Column(length = 50)
    private String parentesco;

    @Column(length = 200)
    private String direccion;

    @OneToMany(mappedBy = "apoderado", cascade = CascadeType.ALL)
    private List<AlumnoApoderado> alumnos = new ArrayList<>();

    public Apoderado() {}

    public static Apoderado desde(ApoderadoDTO dto, Usuario usuario) {
        Apoderado apoderado = new Apoderado();
        apoderado.setUsuario(usuario);
        apoderado.setNombre(dto.getNombre());
        apoderado.setApellido(dto.getApellido());
        apoderado.setFechaNacimiento(dto.getFechaNacimiento());
        apoderado.setEdad(dto.getEdad());
        apoderado.setTelefono(dto.getTelefono());
        apoderado.setTelefonoEmergencia(dto.getTelefonoEmergencia());
        apoderado.setCorreo(dto.getCorreo());
        apoderado.setParentesco(dto.getParentesco());
        apoderado.setDireccion(dto.getDireccion());
        return apoderado;
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

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTelefonoEmergencia() { return telefonoEmergencia; }
    public void setTelefonoEmergencia(String telefonoEmergencia) { this.telefonoEmergencia = telefonoEmergencia; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getParentesco() { return parentesco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public List<AlumnoApoderado> getAlumnos() { return alumnos; }
    public void setAlumnos(List<AlumnoApoderado> alumnos) { this.alumnos = alumnos; }
}
