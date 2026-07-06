package com.example.sistemavalidacionqrs.model.Login;

public class AuthResponse {

    private String token;
    private Integer usuarioId;
    private String matricula;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;

    public AuthResponse() {
    }

    public AuthResponse(String token, Integer usuarioId,String matricula, String nombre,String apellido, String email, String rol) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getMatricula() {return matricula;}

    public void setMatricula(String matricula) {this.matricula = matricula;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void SetApellido(String apellido) {this.apellido = apellido;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
