package com.pablo.authservice.dto;

public class LoginDTO {

    private Long id;
    private String rut;
    private String contrasena;
    private String nombreRol;
    private String token;

    public LoginDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
