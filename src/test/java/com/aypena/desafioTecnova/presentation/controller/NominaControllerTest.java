package com.aypena.desafioTecnova.presentation.controller;


import com.aypena.desafioTecnova.domain.usecase.ProcesarNominaUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NominaController.class)
class NominaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProcesarNominaUseCase procesarNominaUseCase;

    @Test
    void procesarNomina_deberiaRetornar200_OK() throws Exception {
        // Arrange
        String ruta = "src/main/resources/input/empleados.csv";

        // Act & Assert
        mockMvc.perform(post("/api/nomina/procesar")
                        .param("archivoNombre", "empleados.csv")
                        .param("ruta", ruta))
                .andExpect(status().isOk())
                .andExpect(content().string("Procesamiento completado. Revisa los CSV en resources/output."));

        // Verifica que se haya llamado al use case con la ruta correcta
        Mockito.verify(procesarNominaUseCase).ejecutar(eq(Path.of(ruta)));
    }


}