package com.alura.conversorDeMonedas.principal;

import com.alura.conversorDeMonedas.api.ApiClient;
import com.alura.conversorDeMonedas.io.FileManager;
import com.alura.conversorDeMonedas.services.ConversorService;
import com.alura.conversorDeMonedas.modelos.ConversionModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class principalConversor {
    public static void main(String[] args) {
        ApiClient apiClient = new ApiClient();
        FileManager fileManager = new FileManager();
        ConversorService conversorService = new ConversorService(apiClient, fileManager);
        Scanner scanner = new Scanner(System.in);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (true) {
            System.out.println("***********************************************");
            System.out.println("Bienvenido al Conversor de Monedas =)");
            System.out.println("1: Dólar a Peso Argentino");
            System.out.println("2: Peso Argentino a Dólar");
            System.out.println("3: Dólar a Real Brasileño");
            System.out.println("4: Real Brasileño a Dólar");
            System.out.println("5: Dólar a Peso Colombiano");
            System.out.println("6: Peso Colombiano a Dólar");
            System.out.println("7: Salir");
            System.out.println("Escriba el número de opción de conversión que desea:");
            System.out.println("***********************************************");

            String opcion = scanner.nextLine();

            // Verificar si el usuario desea salir antes de pedir cantidad
            if (opcion.equals("7")) {
                try {
                    fileManager.writeToJson("registroConversiones.json");
                    System.out.println("registro de conversiones guardado correctamente");
                } catch (IOException e) {
                    System.out.println("Error al guardar el registro de conversiones: " + e.getMessage());
                }
                try {
                    fileManager.writeErrorsToJson("erroresConversion.json");
                    System.out.println("registro de errores guardado correctamente");
                } catch (IOException e) {
                    System.out.println("Error al guardar el registro de errores: " + e.getMessage());
                }
                System.out.println("Finalizando. Gracias por usar nuestra app!!");
                break;
            }

            // Verificar si la opción es válida
            if (!opcion.matches("[1-6]")) {
                System.out.println("Esta opción no válida. Por favor elige un número del 1 al 7.");
                continue; // Volver a mostrar el menú
            }

            System.out.println("Ingrese la cantidad a convertir:");
            String cantidadCliente = scanner.nextLine();

            try {
                // Ejecutar la conversión
                double cantidad = Double.parseDouble(cantidadCliente);
                ConversionModel conversion = conversorService.realizarConversion(opcion, cantidad, dtf.format(LocalDateTime.now()));
                System.out.println("El valor: " + cantidad + " [" + conversion.getBaseCode() + "] corresponde al valor final de " + conversion.getConversionResult() + " [" + conversion.getTargetCode() + "]");

            } catch (IllegalArgumentException e) {
                String errorMsg = "Error: " + e.getMessage();
                System.out.println(errorMsg);
                fileManager.addError(dtf.format(LocalDateTime.now()) + " - " + errorMsg + " (Ingreso del usuario: " + cantidadCliente + ")"); // Registrar el error
            } catch (IOException | InterruptedException e) {
                String errorMsg = "Error al obtener la cotización: " + e.getMessage();
                System.out.println(errorMsg);
                fileManager.addError(dtf.format(LocalDateTime.now()) + " - " + errorMsg + " (Ingreso del usuario: " + cantidadCliente + ")"); // Registrar el error
            } catch (Exception e) {
                String errorMsg = "Error: La cantidad ingresada no es válida.";
                System.out.println(errorMsg);
                fileManager.addError(dtf.format(LocalDateTime.now()) + " - " + errorMsg + " (Ingreso del usuario: " + cantidadCliente + ")"); // Registrar el error
            }
            System.out.println("URL de consulta a la API 'ExchangeRate-API': " + apiClient.getLastUsedUrl());
        }
        scanner.close();
    }
}
