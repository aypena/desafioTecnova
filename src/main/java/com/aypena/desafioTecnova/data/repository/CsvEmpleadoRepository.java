package com.aypena.desafioTecnova.data.repository;


import com.aypena.desafioTecnova.data.impl.ProcesarNominaUseCaseImpl;
import com.aypena.desafioTecnova.domain.model.Empleado;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
@Slf4j
public class CsvEmpleadoRepository {
    //acceso a datos

    public List<Empleado> leerEmpleados(Path rutaArchivo) throws IOException {
        List<Empleado> empleados = new ArrayList<>();
            //Se leen los Archivos linea por linea            -                    evita problema con acentos u otros caracteres especiales
        try (BufferedReader br = Files.newBufferedReader(rutaArchivo, java.nio.charset.Charset.forName("windows-1252"))) {
            String linea;
            int lineaNum = 0;
            while ((linea = br.readLine()) != null) {
                lineaNum++;
                if (linea.trim().isEmpty()) continue; // saltar líneas vacías
                if (lineaNum == 1 && linea.toLowerCase().contains("nombre")) {
                    // Asumo que hay header ya que encuentra la varable nombre, lo ignoro
                    continue;
                }
                // Separar campos y verificar formato, se valida que se divida cada campo por comas  en un arreglo de 8 campos
                String[] campos = linea.split(",");
                if (campos.length != 8) {
                    log.warn("Línea {} ignorada por número incorrecto de columnas", lineaNum);
                    continue;
                }

                try {
                    //se convierte la linea a un objeto de tipo empleado
                    Empleado emp = mapearEmpleado(campos);
                    empleados.add(emp);
                } catch (Exception e) {
                    log.warn("Error parseando línea {}: {}", lineaNum, e.getMessage());
                }
            }
        }
        return empleados;
    }

    //metodo utilizado para mapear la line del archivo leido a un registro del objeto empleado
    private Empleado mapearEmpleado(String[] campos) {
        return Empleado.builder()
                .nombre(campos[0].trim())
                .apellido(campos[1].trim())
                .rut(campos[2].trim())
                .cargo(campos[3].trim())
                .salarioBase(new BigDecimal(campos[4].trim()))
                .bonos(new BigDecimal(campos[5].trim()))
                .descuentos(new BigDecimal(campos[6].trim()))
                .fechaIngreso(LocalDate.parse(campos[7].trim()))
                .build();
    }
}