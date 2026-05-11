package com.pablo.microservicio.dto;

import com.pablo.microservicio.model.Usuario;

public class LoginDTO {

    private Long id;
    private String rut;
    private String contrasena;
    private String nombre;
    private String apellido;
    private String nombreRol;
    private String token;

    public LoginDTO() {}

    public static LoginDTO desde(Usuario u, String token, String nombre, String apellido) {
        LoginDTO dto = new LoginDTO();
        dto.setId(u.getId());
        dto.setRut(u.getRut());
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setNombreRol(u.getRol().getNombre());
        dto.setToken(token);
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
