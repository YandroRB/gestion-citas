package com.yandrorb.gestion_citas.exception;

public class UsuarioExistente extends RuntimeException {
    public UsuarioExistente(String message) {
        super(message);
    }
}
