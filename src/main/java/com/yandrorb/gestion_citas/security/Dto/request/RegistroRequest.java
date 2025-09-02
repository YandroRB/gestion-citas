package com.yandrorb.gestion_citas.security.Dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistroRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @NotBlank(message = "El número de celular es obligatorio")
    @Pattern(regexp = "\\d{10}", message = "El numero de celular debe tener 10 digitos")
    private String num_celular;
    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "\\d{10}", message = "El numero de celular debe tener 10 digitos")
    private String cedula;
    @Valid
    @NotNull(message = "Los datos de usuario son obligatorios")
    private UsuarioRequest usuario;
}
