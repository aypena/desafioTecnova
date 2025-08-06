package com.aypena.desafioTecnova.domain.usecase;


import com.aypena.desafioTecnova.domain.model.Empleado;
import com.aypena.desafioTecnova.presentation.exception.ValidacionEmpleadoException;

import java.util.List;
import java.util.Set;

public interface NominaService {

    void validarEmpleado(Empleado emp, Set<String> ruts) throws ValidacionEmpleadoException;

    void calcularAntiguedadYSalario(Empleado emp);

    void imprimirEstadisticas(List<Empleado> validos, List<String> invalidos);
}
