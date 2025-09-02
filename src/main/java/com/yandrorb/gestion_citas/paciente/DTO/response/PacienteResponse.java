package com.yandrorb.gestion_citas.paciente.DTO.response;

import com.yandrorb.gestion_citas.usuario.Dto.response.UsuarioResponse;
import lombok.Data;

@Data
public class PacienteResponse {
    private String nombres;
    private String apellidos;
    private String cedula;
    private String num_celular;
    private UsuarioResponse usuario;
}
