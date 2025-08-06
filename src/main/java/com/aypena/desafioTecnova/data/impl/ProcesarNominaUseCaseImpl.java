package com.aypena.desafioTecnova.data.impl;

import com.aypena.desafioTecnova.data.repository.CsvEmpleadoRepository;
import com.aypena.desafioTecnova.data.repository.CsvSalidaRepository;
import com.aypena.desafioTecnova.domain.model.Empleado;
import com.aypena.desafioTecnova.domain.usecase.NominaService;
import com.aypena.desafioTecnova.domain.usecase.ProcesarNominaUseCase;
import com.aypena.desafioTecnova.presentation.exception.ValidacionEmpleadoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProcesarNominaUseCaseImpl implements ProcesarNominaUseCase {

    private final CsvEmpleadoRepository empleadoRepository;
    private final CsvSalidaRepository salidaRepository;
    // Inyectamos el servicio de dominio con la lógica
    private final NominaService nominaService;
    @Override
    public void ejecutar(Path rutaCsvEntrada) {
        try {
            //se implementa el llenado de la lista empleados  a partir de la lectura del archivo dejado en la ruta
            List<Empleado> empleados = empleadoRepository.leerEmpleados(rutaCsvEntrada);
            List<Empleado> empleadosValidos = new ArrayList<>();
            List<String> empleadosInvalidos = new ArrayList<>();

            Set<String> ruts = new HashSet<>();

            for (Empleado emp : empleados) {
                try {
                    nominaService.validarEmpleado(emp, ruts);
                    nominaService.calcularAntiguedadYSalario(emp);
                    empleadosValidos.add(emp);
                    ruts.add(emp.getRut());
                } catch (ValidacionEmpleadoException ex) {
                    empleadosInvalidos.add(emp.getRut() + ";" + ex.getMessage());
                    log.warn("Empleado inválido RUT {}: {}", emp.getRut(), ex.getMessage());
                }
            }

            Path outputDir = Path.of("src/main/resources/output");
            if (!outputDir.toFile().exists()) {
                outputDir.toFile().mkdirs();
            }

            salidaRepository.escribirEmpleadosValidos(empleadosValidos, outputDir.resolve("empleados_validos.csv"));
            salidaRepository.escribirEmpleadosInvalidos(empleadosInvalidos, outputDir.resolve("empleados_invalidos.csv"));

            nominaService.imprimirEstadisticas(empleadosValidos, empleadosInvalidos);

        } catch (IOException e) {
            log.error("Error leyendo/escribiendo archivos", e);
            throw new RuntimeException("Error procesando la nómina, revise incoherencias en el archivo a leer como por ejemplo el nombre: " + e.getMessage());
        }
    }


}
