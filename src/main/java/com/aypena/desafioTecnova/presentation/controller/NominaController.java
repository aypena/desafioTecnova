package com.aypena.desafioTecnova.presentation.controller;

import com.aypena.desafioTecnova.domain.usecase.ProcesarNominaUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;

@RestController
@RequestMapping("/api/nomina")
@AllArgsConstructor
public class NominaController {

    private final ProcesarNominaUseCase procesarNominaUseCase;


    @PostMapping("/procesar")
    public ResponseEntity<String> procesar(@RequestParam String archivoNombre) {
        try {
            procesarNominaUseCase.ejecutar(Paths.get("src/main/resources/input", archivoNombre));
            return ResponseEntity.ok("Procesamiento completado. Revisa los CSV en resources/output.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}