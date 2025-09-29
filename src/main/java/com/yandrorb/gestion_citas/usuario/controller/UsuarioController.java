package com.yandrorb.gestion_citas.usuario.controller;

import com.yandrorb.gestion_citas.usuario.Dto.request.ActualizarUsuarioRequest;
import com.yandrorb.gestion_citas.usuario.serivce.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    @PutMapping
    public ResponseEntity<Map<String,Object>> actualizarCredenciales(@Valid @RequestBody ActualizarUsuarioRequest request, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(usuarioService.actualizarCredenciales(request, username));
    }

    @PutMapping("/{username}/desactivar")
    @PreAuthorize("hasRole('ROLE_RECEPCIONISTA')")
    public ResponseEntity<String> desactivarUsuario(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.desactivarUsuario(username));
    }
    @PutMapping("/{username}/activar")
    @PreAuthorize("hasRole('ROLE_RECEPCIONISTA')")
    public ResponseEntity<String> activarUsuario(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.activarUsuario(username));
    }
}
