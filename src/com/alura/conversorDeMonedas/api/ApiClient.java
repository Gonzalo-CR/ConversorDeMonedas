package com.alura.conversorDeMonedas.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiClient {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/110ffbea640aac9725171a41/pair/";
    private final HttpClient client;
    private String lastUsedUrl;

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

    public double getCotizacionByOpcion(String resultadoOpcionCliente, String cantidadCliente) throws IOException, InterruptedException {
        String url = BASE_URL + resultadoOpcionCliente + cantidadCliente;

        lastUsedUrl = url;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            return jsonResponse.get("conversion_result").getAsDouble();
        } else {
            throw new IOException("Error en la respuesta de la API: Código " + response.statusCode());
        }
    }
    public String getLastUsedUrl() {
        return lastUsedUrl;
    }
}