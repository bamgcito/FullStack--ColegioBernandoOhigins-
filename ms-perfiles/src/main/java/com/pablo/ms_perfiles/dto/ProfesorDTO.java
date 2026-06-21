package com.pablo.ms_perfiles.dto;

import com.pablo.ms_perfiles.model.Profesor;
import java.time.LocalDateTime;

public class ProfesorDTO {

    private Long id;
    private Long usuarioId;
    private String rut;
    private String nombre;
    private String apellido;
    private String especialidad;
    private LocalDateTime fechaCreacion;

    public ProfesorDTO() {}

    public static ProfesorDTO desde(Profesor p) {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(p.getId());
        dto.setUsuarioId(p.getUsuarioId());
        dto.setRut(p.getRut());
        dto.setNombre(p.getNombre());
        dto.setApellido(p.getApellido());
        dto.setEspecialidad(p.getEspecialidad());
        dto.setFechaCreacion(p.getFechaCreacion());
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

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
