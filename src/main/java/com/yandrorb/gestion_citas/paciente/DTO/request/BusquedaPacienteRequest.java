package com.yandrorb.gestion_citas.paciente.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaPacienteRequest {
    private String cedula;
    private String nombre;
    private String apellido;
    private String username;
}
