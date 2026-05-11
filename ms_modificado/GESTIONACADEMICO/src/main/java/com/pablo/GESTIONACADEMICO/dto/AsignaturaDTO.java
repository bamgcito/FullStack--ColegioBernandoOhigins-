package com.pablo.GESTIONACADEMICO.dto;

import com.pablo.GESTIONACADEMICO.model.Asignatura;

public class AsignaturaDTO {

    private Long id;
    private String nombre;

    public AsignaturaDTO() {}

    public AsignaturaDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public static AsignaturaDTO desde(Asignatura a) {
        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setId(a.getId());
        dto.setNombre(a.getNombre());
        return dto;
    }
}
