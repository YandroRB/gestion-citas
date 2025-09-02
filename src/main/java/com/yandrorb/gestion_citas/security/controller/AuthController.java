package com.yandrorb.gestion_citas.security.controller;

import com.yandrorb.gestion_citas.security.Dto.request.AuthRequest;
import com.yandrorb.gestion_citas.security.Dto.request.RegistroRequest;
import com.yandrorb.gestion_citas.security.Dto.response.AuthResponse;
import com.yandrorb.gestion_citas.security.service.AuthService;
import com.yandrorb.gestion_citas.usuario.model.Usuario;
import com.yandrorb.gestion_citas.usuario.repository.UsuarioRepository;
import com.yandrorb.gestion_citas.security.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final AuthService authService;

    @PostMapping("/registro")
    public String registrar(@RequestBody RegistroRequest registroRequest) {
        authService.registroUsuario(registroRequest);
        return "Usuario registrado";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response= authService.loginUsuario(authRequest);
        return ResponseEntity.ok(response);
    }
}
