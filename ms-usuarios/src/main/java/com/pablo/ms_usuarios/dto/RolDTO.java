package com.pablo.ms_usuarios.dto;

import com.pablo.ms_usuarios.model.Rol;

public class RolDTO {

    private Long id;
    private String nombre;

    public RolDTO() {}

    public static RolDTO desde(Rol r) {
        RolDTO dto = new RolDTO();
        dto.setId(r.getId());
        dto.setNombre(r.getNombre());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
