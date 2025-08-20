package com.yandrorb.gestion_citas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identificador;
    @Column(nullable = false , unique = true )
    private String username;
    @Column(nullable = false)
    private String password;
}
