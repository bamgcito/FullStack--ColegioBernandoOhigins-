package com.pablo.GESTIONACADEMICO.dto;

public class CursoDTO {

    private Long id;
    private String nivel;
    private String tipo;
    private String letra;
    private Integer anio;

    public CursoDTO() {
    }

    public CursoDTO(Long id, String nivel, String tipo, String letra, Integer anio) {
        this.id = id;
        this.nivel = nivel;
        this.tipo = tipo;
        this.letra = letra;
        this.anio = anio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
}
