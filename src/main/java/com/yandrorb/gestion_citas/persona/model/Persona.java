package com.yandrorb.gestion_citas.persona.model;

import com.yandrorb.gestion_citas.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class Persona {
    private String nombres;
    private String apellidos;
    private String cedula;
    private String num_celular;
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
