package com.pablo.microservicio.model;

import com.pablo.microservicio.dto.AlumnoDTO;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alumnos")
public class Alumno {

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

    @Column(length = 200)
    private String direccion;

    @Column(name = "fecha_matricula")
    private LocalDate fechaMatricula;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL)
    private List<AlumnoApoderado> apoderados = new ArrayList<>();

    public Alumno() {}

    public static Alumno desde(AlumnoDTO dto, Usuario usuario) {
        Alumno alumno = new Alumno();
        alumno.setUsuario(usuario);
        alumno.setNombre(dto.getNombre());
        alumno.setApellido(dto.getApellido());
        alumno.setFechaNacimiento(dto.getFechaNacimiento());
        alumno.setEdad(dto.getEdad());
        alumno.setDireccion(dto.getDireccion());
        alumno.setFechaMatricula(dto.getFechaMatricula());
        return alumno;
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

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDate getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(LocalDate fechaMatricula) { this.fechaMatricula = fechaMatricula; }

    public List<AlumnoApoderado> getApoderados() { return apoderados; }
    public void setApoderados(List<AlumnoApoderado> apoderados) { this.apoderados = apoderados; }
}
