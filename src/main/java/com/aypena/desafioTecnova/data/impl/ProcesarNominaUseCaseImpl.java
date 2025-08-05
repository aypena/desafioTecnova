package com.aypena.desafioTecnova.data.impl;

import com.aypena.desafioTecnova.data.repository.CsvEmpleadoRepository;
import com.aypena.desafioTecnova.data.repository.CsvSalidaRepository;
import com.aypena.desafioTecnova.domain.model.Empleado;
import com.aypena.desafioTecnova.domain.usecase.ProcesarNominaUseCase;
import com.aypena.desafioTecnova.presentation.exception.ValidacionEmpleadoException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;
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
                    validarEmpleado(emp, ruts);
                    calcularAntiguedadYSalario(emp);
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

            imprimirEstadisticas(empleadosValidos, empleadosInvalidos);

        } catch (IOException e) {
            log.error("Error leyendo/escribiendo archivos", e);
            throw new RuntimeException("Error procesando la nómina, revise incoherencias en el archivo a leer como por ejemplo el nombre: " + e.getMessage());
        }
    }

    private void validarEmpleado(Empleado emp, Set<String> ruts) {
        //como se usa un set , este no permite dublicado y hace la validacion acontinuacion
        if (ruts.contains(emp.getRut())) {
            throw new ValidacionEmpleadoException("RUT duplicado");
        }
        if (emp.getSalarioBase().compareTo(BigDecimal.valueOf(400000)) < 0) {
            throw new ValidacionEmpleadoException("Salario base menor a 400000");
        }
        if (emp.getBonos().compareTo(emp.getSalarioBase().multiply(BigDecimal.valueOf(0.5))) > 0) {
            throw new ValidacionEmpleadoException("Bonos mayores al 50% del salario base");
        }
        if (emp.getDescuentos().compareTo(emp.getSalarioBase()) > 0) {
            throw new ValidacionEmpleadoException("Descuentos mayores al salario base");
        }
        if (emp.getFechaIngreso().isAfter(LocalDate.now())) {
            throw new ValidacionEmpleadoException("Fecha de ingreso futura");
        }
    }

    private void calcularAntiguedadYSalario(Empleado emp) {
        Period periodo = Period.between(emp.getFechaIngreso(), LocalDate.now());
        int anios = periodo.getYears();
        emp.setAntiguedad(anios);

        BigDecimal bonificacion = BigDecimal.ZERO;
        if (anios > 5) {
            bonificacion = emp.getSalarioBase().multiply(BigDecimal.valueOf(0.10));
        } else if (anios >= 3) {
            bonificacion = emp.getSalarioBase().multiply(BigDecimal.valueOf(0.05));
        }
        emp.setBonificacionAntiguedad(bonificacion);

        BigDecimal salarioFinal = emp.getSalarioBase()
                .add(emp.getBonos())
                .add(bonificacion)
                .subtract(emp.getDescuentos());

        emp.setSalarioFinal(salarioFinal);
    }

    private void imprimirEstadisticas(List<Empleado> validos, List<String> invalidos) {
        System.out.println("Total empleados válidos: " + validos.size());
        System.out.println("Total empleados inválidos: " + invalidos.size());

        if (!validos.isEmpty()) {
            BigDecimal sumaSalarios = validos.stream()
                    .map(Empleado::getSalarioFinal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            double promedioSalarios = sumaSalarios.doubleValue() / validos.size();

            double promedioAntiguedad = validos.stream()
                    .mapToInt(Empleado::getAntiguedad)
                    .average()
                    .orElse(0);

            System.out.printf("Promedio salario final: %.2f\n", promedioSalarios);
            System.out.printf("Promedio antigüedad (años): %.2f\n", promedioAntiguedad);
        }
    }
}
