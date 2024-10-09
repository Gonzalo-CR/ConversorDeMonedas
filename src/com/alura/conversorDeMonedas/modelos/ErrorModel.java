package com.alura.conversorDeMonedas.modelos;

public class ErrorModel {
    private String fechaHora; // Fecha y hora del error
    private String mensajeError; // Mensaje descriptivo del error
    private String inputUsuario; // El valor ingresado por el usuario (opcional)

    // Constructor
    public ErrorModel(String fechaHora, String mensajeError, String inputUsuario) {
        this.fechaHora = fechaHora;
        this.mensajeError = mensajeError;
        this.inputUsuario = inputUsuario;
    }

    // Getters
    public String getFechaHora() {
        return fechaHora;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public String getInputUsuario() {
        return inputUsuario;
    }
}

