package com.pablo.ms_usuarios.dto;

import com.pablo.ms_usuarios.model.Usuario;
import java.time.LocalDateTime;

public class UsuarioDTO {

    private Long id;
    private String rut;
    private String contrasena;
    private Long rolId;
    private String nombreRol;
    private LocalDateTime fechaCreacion;

    public UsuarioDTO() {}

    public static UsuarioDTO desde(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setRut(u.getRut());
        dto.setContrasena(u.getContrasena());
        dto.setNombreRol(u.getRol().getNombre());
        dto.setFechaCreacion(u.getFechaCreacion());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
