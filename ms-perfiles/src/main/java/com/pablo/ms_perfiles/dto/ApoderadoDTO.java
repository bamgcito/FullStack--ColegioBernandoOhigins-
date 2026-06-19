package com.pablo.ms_perfiles.dto;

import com.pablo.ms_perfiles.model.Apoderado;
import java.time.LocalDateTime;

public class ApoderadoDTO {

    private Long id;
    private Long usuarioId;
    private String rut;
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDateTime fechaCreacion;

    public ApoderadoDTO() {}

    public static ApoderadoDTO desde(Apoderado a) {
        ApoderadoDTO dto = new ApoderadoDTO();
        dto.setId(a.getId());
        dto.setUsuarioId(a.getUsuarioId());
        dto.setRut(a.getRut());
        dto.setNombre(a.getNombre());
        dto.setApellido(a.getApellido());
        dto.setTelefono(a.getTelefono());
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

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
