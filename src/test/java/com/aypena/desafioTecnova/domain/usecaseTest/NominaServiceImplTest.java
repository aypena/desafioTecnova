package com.aypena.desafioTecnova.domain.usecaseTest;

import com.aypena.desafioTecnova.domain.model.Empleado;
import com.aypena.desafioTecnova.domain.usercaseImpl.NominaServiceImpl;
import com.aypena.desafioTecnova.presentation.exception.ValidacionEmpleadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NominaServiceImplTest {

    private NominaServiceImpl nominaService;
    private Empleado empleadoBase;

    @BeforeEach
    void setUp() {
        nominaService = new NominaServiceImpl();
        empleadoBase = Empleado.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .rut("12345678-9")
                .cargo("Analista")
                .salarioBase(BigDecimal.valueOf(500000))
                .bonos(BigDecimal.valueOf(100000))
                .descuentos(BigDecimal.valueOf(50000))
                .fechaIngreso(LocalDate.now().minusYears(4))
                .build();
    }

    @Test
    void validarEmpleado_conDatosCorrectos_noLanzaExcepcion() {
        Set<String> ruts = new HashSet<>();
        assertDoesNotThrow(() -> nominaService.validarEmpleado(empleadoBase, ruts));
    }

    @Test
    void validarEmpleado_conRUTDuplicado_lanzaExcepcion() {
        Set<String> ruts = new HashSet<>();
        ruts.add("12345678-9");

        ValidacionEmpleadoException ex = assertThrows(
                ValidacionEmpleadoException.class,
                () -> nominaService.validarEmpleado(empleadoBase, ruts)
        );

        assertEquals("RUT duplicado", ex.getMessage());
    }


    @Test
    void validarEmpleado_conBonosMayoresAl50_lanzaExcepcion() {
        empleadoBase.setBonos(BigDecimal.valueOf(300000)); // > 50% de 500000
        Set<String> ruts = new HashSet<>();

        ValidacionEmpleadoException ex = assertThrows(
                ValidacionEmpleadoException.class,
                () -> nominaService.validarEmpleado(empleadoBase, ruts)
        );

        assertEquals("Bonos mayores al 50% del salario base", ex.getMessage());
    }

    @Test
    void validarEmpleado_conDescuentoMayorASalario_lanzaExcepcion() {
        empleadoBase.setDescuentos(BigDecimal.valueOf(600000)); // > salario base
        Set<String> ruts = new HashSet<>();

        ValidacionEmpleadoException ex = assertThrows(
                ValidacionEmpleadoException.class,
                () -> nominaService.validarEmpleado(empleadoBase, ruts)
        );

        assertEquals("Descuentos mayores al salario base", ex.getMessage());
    }

    @Test
    void validarEmpleado_conFechaIngresoFutura_lanzaExcepcion() {
        empleadoBase.setFechaIngreso(LocalDate.now().plusDays(1));
        Set<String> ruts = new HashSet<>();

        ValidacionEmpleadoException ex = assertThrows(
                ValidacionEmpleadoException.class,
                () -> nominaService.validarEmpleado(empleadoBase, ruts)
        );

        assertEquals("Fecha de ingreso futura", ex.getMessage());
    }




}
