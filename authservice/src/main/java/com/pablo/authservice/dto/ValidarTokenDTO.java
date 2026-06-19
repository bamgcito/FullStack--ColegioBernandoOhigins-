package com.pablo.authservice.dto;

public class ValidarTokenDTO {

    private boolean valido;
    private String rut;
    private String rol;

    public ValidarTokenDTO() {}

    public boolean isValido() { return valido; }
    public void setValido(boolean valido) { this.valido = valido; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
