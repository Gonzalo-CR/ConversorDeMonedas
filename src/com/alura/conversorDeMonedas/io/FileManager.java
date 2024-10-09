package com.alura.conversorDeMonedas.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private List<String> registros = new ArrayList<>();
    private List<String> errores = new ArrayList<>(); // Lista para registrar errores

    public void addRegistro(String registro) {
        registros.add(registro);
    }

    public void addError(String error) {
        errores.add(error); // Agregar error a la lista
    }

    public void writeToJson(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("[\n");
            for (int i = 0; i < registros.size(); i++) {
                writer.write("  \"" + registros.get(i) + "\"");
                if (i < registros.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]");
        }
    }

    public void writeErrorsToJson(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("[\n");
            for (int i = 0; i < errores.size(); i++) {
                writer.write("  \"" + errores.get(i) + "\"");
                if (i < errores.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]");
        }
    }
}
