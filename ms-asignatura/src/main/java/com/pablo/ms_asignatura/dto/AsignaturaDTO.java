package com.pablo.ms_asignatura.dto;

import com.pablo.ms_asignatura.model.Asignatura;
import java.time.LocalDateTime;

public class AsignaturaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;

    public static AsignaturaDTO desde(Asignatura a) {
        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setId(a.getId());
        dto.setNombre(a.getNombre());
        dto.setDescripcion(a.getDescripcion());
        dto.setFechaCreacion(a.getFechaCreacion());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
