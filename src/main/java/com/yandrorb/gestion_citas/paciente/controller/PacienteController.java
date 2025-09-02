package com.yandrorb.gestion_citas.paciente.controller;

import com.yandrorb.gestion_citas.paciente.DTO.response.PacienteResponse;
import com.yandrorb.gestion_citas.paciente.model.Paciente;
import com.yandrorb.gestion_citas.paciente.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paciente")
@RequiredArgsConstructor
public class PacienteController {
    private final PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<PacienteResponse> verDatosPaciente(Authentication authentication) {
        String nombre = authentication.getName();
        return ResponseEntity.ok(pacienteService.obtenerDatosPciente(nombre));
    }
}
