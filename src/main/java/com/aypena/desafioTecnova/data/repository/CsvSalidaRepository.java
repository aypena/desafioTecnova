package com.aypena.desafioTecnova.data.repository;

import com.aypena.desafioTecnova.domain.model.Empleado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Repository
public class CsvSalidaRepository {

    public void escribirEmpleadosValidos(List<Empleado> empleados, Path rutaArchivo) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(rutaArchivo)) {
            // Header
            bw.write("Nombre;Apellido;RUT;Cargo;SalarioBase;Bonos;BonificacionAntiguedad;Descuentos;SalarioFinal;AntiguedadAnios");
            bw.newLine();

            for (Empleado e : empleados) {
                bw.write(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%d",
                        e.getNombre(),
                        e.getApellido(),
                        e.getRut(),
                        e.getCargo(),
                        e.getSalarioBase().setScale(2, RoundingMode.HALF_UP),
                        e.getBonos().setScale(2, RoundingMode.HALF_UP),
                        e.getBonificacionAntiguedad().setScale(2, RoundingMode.HALF_UP),
                        e.getDescuentos().setScale(2, RoundingMode.HALF_UP),
                        e.getSalarioFinal().setScale(2, RoundingMode.HALF_UP),
                        e.getAntiguedad()
                ));
                bw.newLine();
            }
        }
    }

    public void escribirEmpleadosInvalidos(List<String> lineasInvalidas, Path rutaArchivo) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(rutaArchivo)) {
            bw.write("Linea;Motivo");
            bw.newLine();
            for (String linea : lineasInvalidas) {
                bw.write(linea);
                bw.newLine();
            }
        }
    }
}
