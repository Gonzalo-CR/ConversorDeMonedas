package com.alura.conversorDeMonedas.io;

import com.alura.conversorDeMonedas.modelos.ConversionModel;
import com.alura.conversorDeMonedas.modelos.ErrorModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private List<ConversionModel> registros = new ArrayList<>();  // Lista para ConversionModel
    private List<ErrorModel> errores = new ArrayList<>(); // Lista para ErrorModel

    // Metodo para agregar un registro de conversión
    public void addRegistro(ConversionModel registro) {
        registros.add(registro);
    }

    // Metodo para agregar un error
    public void addError(ErrorModel error) {
        errores.add(error); // Agregar ErrorModel a la lista de errores
    }

    // Metodo para escribir registros de conversiones en JSON
    public void writeToJson(String filename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Usar Gson para formatear JSON
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(gson.toJson(registros)); // Guardar lista de ConversionModel como JSON
        }
    }

    // Metodo para escribir errores en JSON
    public void writeErrorsToJson(String filename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Usar Gson para formatear JSON
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(gson.toJson(errores)); // Guardar lista de ErrorModel como JSON
        }
    }
}
