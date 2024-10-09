package com.alura.conversorDeMonedas.services;

import com.alura.conversorDeMonedas.api.ApiClient;
import com.alura.conversorDeMonedas.io.FileManager;
import com.alura.conversorDeMonedas.modelos.ConversionModel;

import java.io.IOException;

public class ConversorService {
    private final ApiClient apiClient;
    private final FileManager fileManager;

    public ConversorService(ApiClient apiClient, FileManager fileManager) {
        this.apiClient = apiClient;
        this.fileManager = fileManager;
    }

    // Método para realizar la conversión usando los valores obtenidos de la API
    public ConversionModel realizarConversion(String opcionCliente, double cantidad, String fechaHora) throws IOException, InterruptedException {
        String resultadoOpcionCliente = apiClient.resultadoOpcionCliente(opcionCliente);

        // Obtener los valores de la API: conversion_rate y conversion_result
        ApiClient.ConversionResponse conversionResponse = apiClient.getCotizacionByOpcion(resultadoOpcionCliente, String.valueOf(cantidad));

        String baseCode = resultadoOpcionCliente.split("/")[0];
        String targetCode = resultadoOpcionCliente.split("/")[1];

        // Crear el modelo de conversión con los valores obtenidos de la API
        ConversionModel conversion = new ConversionModel(
                baseCode,
                targetCode,
                conversionResponse.getConversionRate(), // Usar la tasa de conversión de la API
                conversionResponse.getConversionResult(), // Usar el resultado de la conversión de la API
                fechaHora,
                cantidad
        );

        // Agregar el registro al FileManager
        fileManager.addRegistro(conversion);

        return conversion;
    }
}
