package com.aypena.desafioTecnova.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {
    private String nombre;
    private String apellido;
    private String rut;
    private String cargo;
    private BigDecimal salarioBase;
    private BigDecimal bonos;
    private BigDecimal descuentos;
    private LocalDate fechaIngreso;

    // Campos calculados
    private int antiguedad;
    private BigDecimal bonificacionAntiguedad;
    private BigDecimal salarioFinal;


}
