package com.aypena.desafioTecnova.domain.usecase;

import java.nio.file.Path;

public interface ProcesarNominaUseCase {
    void ejecutar(Path rutaCsvEntrada);
}
