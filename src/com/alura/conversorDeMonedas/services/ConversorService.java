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

    public ConversionModel realizarConversion(String opcionCliente, double cantidad, String fechaHora) throws IOException, InterruptedException {
        String resultadoOpcionCliente = apiClient.resultadoOpcionCliente(opcionCliente);
        double conversionResult = apiClient.getCotizacionByOpcion(resultadoOpcionCliente, String.valueOf(cantidad));

        String baseCode = resultadoOpcionCliente.split("/")[0];
        String targetCode = resultadoOpcionCliente.split("/")[1];
        double conversionRate = conversionResult / cantidad; // Se puede calcular la tasa de conversión

        ConversionModel conversion = new ConversionModel(baseCode, targetCode, conversionRate, conversionResult, fechaHora, cantidad);
        fileManager.addRegistro(conversion.toRegistroString()); // Agregar registro a FileManager


        return conversion;
    }


}
