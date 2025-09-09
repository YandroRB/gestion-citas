package com.yandrorb.gestion_citas.usuario.Dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActualizarUsuarioRequest {
    private String username;
    private String password;
    private String correo;
    @NotBlank(message = "Debe proporcionar la contraseña actual.")
    private String currentPassword;

    @AssertTrue(message = "Debe enviar al menos uno de los siguientes campos: username, password o correo")
    public boolean tieneCampos(){
        return username !=null || password !=null || correo !=null;
    }
}
