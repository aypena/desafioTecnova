package com.aypena.desafioTecnova.domain.usercaseImpl;


import com.aypena.desafioTecnova.domain.model.Empleado;
import com.aypena.desafioTecnova.domain.usecase.NominaService;
import com.aypena.desafioTecnova.presentation.exception.ValidacionEmpleadoException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;

@Service
public class NominaServiceImpl implements NominaService {

    @Override
    public void validarEmpleado(Empleado emp, Set<String> ruts) throws ValidacionEmpleadoException {
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

    @Override
    public void calcularAntiguedadYSalario(Empleado emp) {
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

    @Override
    public void imprimirEstadisticas(List<Empleado> validos, List<String> invalidos) {
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