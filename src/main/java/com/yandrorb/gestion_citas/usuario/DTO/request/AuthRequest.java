package com.yandrorb.gestion_citas.usuario.DTO.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
