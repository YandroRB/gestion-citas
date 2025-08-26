package com.yandrorb.gestion_citas.persona.model;

import com.yandrorb.gestion_citas.usuario.model.Usuario;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class Persona {
    @Column(nullable = false)
    private String nombres;
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false)
    private String cedula;
    @Column(nullable = false)
    private String num_celular;
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
