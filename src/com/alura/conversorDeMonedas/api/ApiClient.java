package com.alura.conversorDeMonedas.api;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.cdimascio.dotenv.Dotenv; // Importa la biblioteca dotenv

public class ApiClient {
    private static final String BASE_URL;
    private final HttpClient client;
    private String lastUsedUrl;

    // Cargar dotenv
    static {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("APIKEY"); // Cargar la APIKEY desde el archivo .env
        BASE_URL = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/";
    }

    public ApiClient() {
        this.client = HttpClient.newHttpClient();
    }

    public String resultadoOpcionCliente(String opcionCliente) {
        switch (opcionCliente) {
            case "1": return "USD/ARS/";
            case "2": return "ARS/USD/";
            case "3": return "USD/BRL/";
            case "4": return "BRL/USD/";
            case "5": return "USD/COP/";
            case "6": return "COP/USD/";
            case "7": return "salir";
            default: throw new IllegalArgumentException("Opción no válida. Por favor elige un número del 1 al 7.");
        }
    }

    // Clase auxiliar para retornar tanto el rate como el resultado
    public static class ConversionResponse {
        private final double conversionRate;
        private final double conversionResult;

        public ConversionResponse(double conversionRate, double conversionResult) {
            this.conversionRate = conversionRate;
            this.conversionResult = conversionResult;
        }

        public double getConversionRate() {
            return conversionRate;
        }

        public double getConversionResult() {
            return conversionResult;
        }
    }

    // Método para obtener conversion_rate y conversion_result de la API
    public ConversionResponse getCotizacionByOpcion(String resultadoOpcionCliente, String cantidadCliente) throws IOException, InterruptedException {
        String url = BASE_URL + resultadoOpcionCliente + cantidadCliente;
        lastUsedUrl = url;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            double conversionRate = jsonResponse.get("conversion_rate").getAsDouble(); // Obtener la tasa de conversión
            double conversionResult = jsonResponse.get("conversion_result").getAsDouble(); // Obtener el resultado de la conversión
            return new ConversionResponse(conversionRate, conversionResult); // Retornar ambos valores
        } else {
            throw new IOException("Error en la respuesta de la API: Código " + response.statusCode());
        }
    }

    public String getLastUsedUrl() {
        return lastUsedUrl;
    }
}
