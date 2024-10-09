package com.alura.conversorDeMonedas.modelos;

public class ConversionModel {
    private String baseCode;
    private String targetCode;
    private double conversionRate;
    private double conversionResult;
    private String fechaHora; // Fecha y hora de la consulta
    private double cantidad; // Cantidad ingresada por el cliente

    // Constructor de inicialización de los atributos de la clase
    public ConversionModel(String baseCode, String targetCode, double conversionRate, double conversionResult, String fechaHora, double cantidad) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.conversionRate = conversionRate;
        this.conversionResult = conversionResult;
        this.fechaHora = fechaHora;
        this.cantidad = cantidad;
    }

    // Getters
    public String getBaseCode() {
        return baseCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public double getConversionResult() {
        return conversionResult;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public double getCantidad() {
        return cantidad;
    }

    // Método para crear un string de registro
    public String toRegistroString() {
        return String.format("{\"fechaHora\": \"%s\", \"convertido\": %.2f %s a %s, \"tasaConversion\": %.4f, \"resultado\": %.2f}",
                fechaHora, cantidad, baseCode, targetCode, conversionRate, conversionResult);
    }
}
