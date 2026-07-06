package com.example.sistemavalidacionqrs.model.Accesos;

import java.time.LocalDateTime;

public class AccesoResponse {


    private Integer id;

    private String matricula;

    private Integer usuarioId;

    private String nombreCompleto;

    private String fechaAcceso;

    private String estado;

    private String ipAddress;

    private String dispositivo;

    private Integer eventoId;

    private String nombreEvento;

    private String codigo;



    public AccesoResponse() {
    }



    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }



    public String getMatricula() {
        return matricula;
    }


    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }



    public Integer getUsuarioId() {
        return usuarioId;
    }


    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }



    public String getNombreCompleto() {
        return nombreCompleto;
    }


    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }



    public String getFechaAcceso() {
        return fechaAcceso;
    }


    public void setFechaAcceso(String fechaAcceso) {
        this.fechaAcceso = fechaAcceso;
    }



    public String getEstado() {
        return estado;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }



    public String getIpAddress() {
        return ipAddress;
    }


    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }



    public String getDispositivo() {
        return dispositivo;
    }


    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }



    public Integer getEventoId() {
        return eventoId;
    }


    public void setEventoId(Integer eventoId) {
        this.eventoId = eventoId;
    }



    public String getNombreEvento() {
        return nombreEvento;
    }


    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }



    public String getCodigo() {
        return codigo;
    }


    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}