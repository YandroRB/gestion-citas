package com.yandrorb.gestion_citas.paciente.controller;

import com.yandrorb.gestion_citas.paciente.DTO.request.ActualizarPacienteRequest;
import com.yandrorb.gestion_citas.paciente.DTO.request.BusquedaPacienteRequest;
import com.yandrorb.gestion_citas.paciente.DTO.response.PacienteResponse;
import com.yandrorb.gestion_citas.paciente.model.Paciente;
import com.yandrorb.gestion_citas.paciente.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paciente")
@RequiredArgsConstructor
public class PacienteController {
    private final PacienteService pacienteService;

    @GetMapping
    @PreAuthorize("hasAuthority('ver_datos_paciente')")
    public ResponseEntity<PacienteResponse> verDatosPaciente(Authentication authentication) {
        String nombre = authentication.getName();
        return ResponseEntity.ok(pacienteService.obtenerDatosPciente(nombre));
    }
    @PutMapping
    @PreAuthorize("hasAuthority('editar_datos_paciente')")
    public ResponseEntity<PacienteResponse> actualizarDatosPaciente(@RequestBody ActualizarPacienteRequest request, Authentication authentication) {
        String nombre = authentication.getName();
        return ResponseEntity.ok(pacienteService.actualizarDatosPaciente(nombre, request));
    }
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ROLE_RECEPCIONISTA')")
    public ResponseEntity<Page<PacienteResponse>> buscarPacientes(
            @RequestParam(required = false) String cedula,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        BusquedaPacienteRequest request = BusquedaPacienteRequest.builder()
                .cedula(cedula)
                .nombre(nombre)
                .apellido(apellido)
                .username(username)
                .build();

        Page<PacienteResponse> response=pacienteService.buscarPaciente(request, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }
}
