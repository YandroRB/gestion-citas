package com.yandrorb.gestion_citas.usuario.controller;

import com.yandrorb.gestion_citas.usuario.Dto.request.ActualizarUsuarioRequest;
import com.yandrorb.gestion_citas.usuario.serivce.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
