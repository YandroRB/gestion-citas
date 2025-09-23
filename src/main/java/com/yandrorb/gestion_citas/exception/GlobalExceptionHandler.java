package com.yandrorb.gestion_citas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioExistente.class)
    public ResponseEntity<Map<String, Object>> usuarioExistente(UsuarioExistente ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error","Usuario existente");
        response.put("mensaje",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<Map<String, Object>> validacion(ValidacionException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error","Datos no validos");
        response.put("mensaje",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwt(io.jsonwebtoken.ExpiredJwtException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Token expirado");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(io.jsonwebtoken.security.SignatureException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidSignature(io.jsonwebtoken.security.SignatureException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Token inválido");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Map<String, Object>> handleDisableUsuario(InternalAuthenticationServiceException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Autentificación");
        body.put("message", ex.getMessage());
        if(ex.getCause() instanceof DisabledException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        System.out.println("Excepción no relacionada con auth: " + ex.getClass().getName());
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Error interno");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}

