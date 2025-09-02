package com.yandrorb.gestion_citas.usuario.Dto.request;

import lombok.Data;

@Data
public class ActualizarUsuarioRequest {
    private String username;
    private String password;
    private String correo;
}
