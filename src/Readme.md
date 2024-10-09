# Resumen General del Código

#### El programa es un conversor de monedas que permite a los usuarios convertir entre varias divisas utilizando una API externa para obtener las tasas de conversión. Se compone de las siguientes clases:

    ApiClient: Maneja la comunicación con la API de tasas de cambio.
    FileManager: Se encarga de registrar las conversiones y los errores en archivos JSON.
    ConversionModel: Representa el modelo de conversión de monedas.
    ConversorService: Contiene la lógica de conversión y coordina la interacción entre las otras clases.
    PrincipalConversor: Contiene el método principal que interactúa con el usuario.



# 
### Ahora, revisemos cada archivo y su contenido:
## 1. ApiClient.java


````java
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
        this.client = HttpClient.newHttpClient(); // Inicializa un cliente HTTP
    }

    public String resultadoOpcionCliente(String opcionCliente) {
        switch (opcionCliente) {
            case "1": return "USD/ARS/"; // Dólar a Peso Argentino
            case "2": return "ARS/USD/"; // Peso Argentino a Dólar
            case "3": return "USD/BRL/"; // Dólar a Real Brasileño
            case "4": return "BRL/USD/"; // Real Brasileño a Dólar
            case "5": return "USD/COP/"; // Dólar a Peso Colombiano
            case "6": return "COP/USD/"; // Peso Colombiano a Dólar
            case "7": return "salir"; // Salir del programa
            default: throw new IllegalArgumentException("Opción no válida. Por favor elige un número del 1 al 7."); // Manejo de errores
        }
    }

    public double getCotizacionByOpcion(String resultadoOpcionCliente, String cantidadCliente) throws IOException, InterruptedException {
        String url = BASE_URL + resultadoOpcionCliente + cantidadCliente; // Construye la URL

        lastUsedUrl = url; // Guarda la última URL utilizada

        HttpRequest request = HttpRequest.newBuilder() // Crea una solicitud HTTP
                .uri(URI.create(url)) // Establece la URI
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); // Envía la solicitud y obtiene la respuesta

        if (response.statusCode() == 200) { // Verifica si la respuesta es exitosa
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject(); // Parsea la respuesta JSON
            return jsonResponse.get("conversion_result").getAsDouble(); // Devuelve el resultado de conversión
        } else {
            throw new IOException("Error en la respuesta de la API: Código " + response.statusCode()); // Manejo de errores
        }
    }

    public String getLastUsedUrl() {
        return lastUsedUrl; // Devuelve la última URL utilizada
    }
    }
```` 


### Descripción del archivo:

    ApiClient: Clase responsable de comunicarse con la API externa. Se encarga de construir las URL para las conversiones y obtener las tasas desde la API.

## 2. FileManager.java


````java
    package com.alura.conversorDeMonedas.io;
    
    import java.io.FileWriter;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;
    
    public class FileManager {
    private List<String> registros = new ArrayList<>(); // Lista para almacenar registros de conversiones
    private List<String> errores = new ArrayList<>(); // Lista para registrar errores

    public void addRegistro(String registro) {
        registros.add(registro); // Agrega un registro a la lista
    }

    public void addError(String error) {
        errores.add(error); // Agrega un error a la lista
    }

    public void writeToJson(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) { // Crea un FileWriter para escribir en el archivo
            writer.write("[\n");
            for (int i = 0; i < registros.size(); i++) {
                writer.write("  \"" + registros.get(i) + "\""); // Escribe cada registro como un string JSON
                if (i < registros.size() - 1) {
                    writer.write(",\n"); // Agrega una coma entre registros
                }
            }
            writer.write("\n]");
        }
    }

    public void writeErrorsToJson(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) { // Crea un FileWriter para escribir en el archivo
            writer.write("[\n");
            for (int i = 0; i < errores.size(); i++) {
                writer.write("  \"" + errores.get(i) + "\""); // Escribe cada error como un string JSON
                if (i < errores.size() - 1) {
                    writer.write(",\n"); // Agrega una coma entre errores
                }
            }
            writer.write("\n]");
        }
    }
    }
````
### Descripción del archivo:

    FileManager: Clase encargada de manejar la escritura de registros y errores en archivos JSON. Proporciona métodos para agregar registros y errores a las listas y para escribirlas en archivos.

## 3. ConversionModel.java


````java
    package com.alura.conversorDeMonedas.modelos;
    
    public class ConversionModel {
    private String baseCode; // Código de la moneda base
    private String targetCode; // Código de la moneda objetivo
    private double conversionRate; // Tasa de conversión
    private double conversionResult; // Resultado de la conversión
    private String fechaHora; // Fecha y hora de la consulta
    private double cantidad; // Cantidad ingresada por el cliente

    public ConversionModel(String baseCode, String targetCode, double conversionRate, double conversionResult, String fechaHora, double cantidad) {
        this.baseCode = baseCode; // Inicializa el código de la moneda base
        this.targetCode = targetCode; // Inicializa el código de la moneda objetivo
        this.conversionRate = conversionRate; // Inicializa la tasa de conversión
        this.conversionResult = conversionResult; // Inicializa el resultado de la conversión
        this.fechaHora = fechaHora; // Inicializa la fecha y hora
        this.cantidad = cantidad; // Inicializa la cantidad ingresada
    }

    // Getters
    public String getBaseCode() {
        return baseCode; // Devuelve el código de la moneda base
    }

    public String getTargetCode() {
        return targetCode; // Devuelve el código de la moneda objetivo
    }

    public double getConversionRate() {
        return conversionRate; // Devuelve la tasa de conversión
    }

    public double getConversionResult() {
        return conversionResult; // Devuelve el resultado de la conversión
    }

    public String getFechaHora() {
        return fechaHora; // Devuelve la fecha y hora
    }

    public double getCantidad() {
        return cantidad; // Devuelve la cantidad ingresada
    }

    public String toRegistroString() {
        return String.format("{\"fechaHora\": \"%s\", \"convertido\": %.2f %s a %s, \"tasaConversion\": %.4f, \"resultado\": %.2f}",
                fechaHora, cantidad, baseCode, targetCode, conversionRate, conversionResult); // Crea un string de registro
    }
    }
````
### Descripción del archivo:

    ConversionModel: Clase que representa el modelo de conversión de monedas. Almacena información sobre las monedas involucradas, la tasa de conversión, el resultado de la conversión, la fecha y hora de la consulta, y la cantidad ingresada.

## 4. ConversorService.java


````java
    package com.alura.conversorDeMonedas.services;
    
    import com.alura.conversorDeMonedas.api.ApiClient;
    import com.alura.conversorDeMonedas.io.FileManager;
    import com.alura.conversorDeMonedas.modelos.ConversionModel;
    
    import java.io.IOException;
    import java.text.SimpleDateFormat;
    import java.util.Date;
    
    public class ConversorService {
    private ApiClient apiClient; // Instancia de ApiClient
    private FileManager fileManager; // Instancia de FileManager

    public ConversorService(ApiClient apiClient, FileManager fileManager) {
        this.apiClient = apiClient; // Inicializa apiClient
        this.fileManager = fileManager; // Inicializa fileManager
    }

    public void realizarConversion(String opcionCliente, String cantidadCliente) throws IOException, InterruptedException {
        String resultadoOpcionCliente = apiClient.resultadoOpcionCliente(opcionCliente); // Obtiene el par de divisas
        double conversionResult = apiClient.getCotizacionByOpcion(resultadoOpcionCliente, cantidadCliente); // Obtiene el resultado de conversión

        // Registra la conversión
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        ConversionModel conversionModel = new ConversionModel(
                resultadoOpcionCliente.split("/")[0],
                resultadoOpcionCliente.split("/")[1],
                conversionResult,
                conversionResult * Double.parseDouble(cantidadCliente),
                fechaHora,
                Double.parseDouble(cantidadCliente)
        );

        fileManager.addRegistro(conversionModel.toRegistroString()); // Agrega el registro al FileManager
        System.out.println("Conversión exitosa: " + conversionModel.getConversionResult());
    }
    }
````
### Descripción del archivo:

    ConversorService: Clase que contiene la lógica de conversión. Maneja la interacción entre ApiClient y FileManager, y registra la conversión.

## 5. PrincipalConversor.java


````java
    package com.alura.conversorDeMonedas;
    
    import com.alura.conversorDeMonedas.api.ApiClient;
    import com.alura.conversorDeMonedas.io.FileManager;
    import com.alura.conversorDeMonedas.services.ConversorService;
    
    import java.io.IOException;
    import java.util.Scanner;
    
    public class PrincipalConversor {
    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    ApiClient apiClient = new ApiClient(); // Crea una instancia de ApiClient
    FileManager fileManager = new FileManager(); // Crea una instancia de FileManager
    ConversorService conversorService = new ConversorService(apiClient, fileManager); // Crea una instancia de ConversorService

        while (true) {
            System.out.println("Seleccione una opción para convertir divisas:");
            System.out.println("1: Dólar a Peso Argentino");
            System.out.println("2: Peso Argentino a Dólar");
            System.out.println("3: Dólar a Real Brasileño");
            System.out.println("4: Real Brasileño a Dólar");
            System.out.println("5: Dólar a Peso Colombiano");
            System.out.println("6: Peso Colombiano a Dólar");
            System.out.println("7: Salir");

            String opcionCliente = scanner.nextLine(); // Lee la opción del usuario

            if (opcionCliente.equals("7")) {
                break; // Sale del ciclo si elige salir
            }

            try {
                System.out.print("Ingrese la cantidad a convertir: ");
                String cantidadCliente = scanner.nextLine(); // Lee la cantidad a convertir
                conversorService.realizarConversion(opcionCliente, cantidadCliente); // Realiza la conversión

            } catch (IOException | InterruptedException e) {
                String errorMessage = "Error en la conversión: " + e.getMessage(); // Manejo de errores
                System.out.println(errorMessage);
                fileManager.addError(errorMessage); // Agrega el error al FileManager
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage()); // Manejo de opciones no válidas
            }
        }

        // Guarda los registros y errores en archivos JSON al salir
        try {
            fileManager.writeToJson("registros.json");
            fileManager.writeErrorsToJson("erroresConversion.json");
        } catch (IOException e) {
            System.out.println("Error al guardar registros: " + e.getMessage()); // Manejo de errores al guardar
        }
    }
}
````
### Descripción del archivo:

    PrincipalConversor: Clase que contiene el método main. 
    Maneja la interacción del usuario y coordina la ejecución del conversor.


# Manejo de Excepciones y Errores