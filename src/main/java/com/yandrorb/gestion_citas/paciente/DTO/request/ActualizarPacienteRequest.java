package com.yandrorb.gestion_citas.paciente.DTO.request;

import lombok.Data;

@Data
public class ActualizarPacienteRequest {
    private String nombres;
    private String apellidos;
    private String cedula;
    private String num_celular;
    public boolean tieneCampos(){
        return nombres!=null||apellidos!=null||cedula!=null||num_celular!=null;
    }
}
