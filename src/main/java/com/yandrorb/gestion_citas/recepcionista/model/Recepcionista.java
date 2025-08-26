package com.yandrorb.gestion_citas.recepcionista.model;

import com.yandrorb.gestion_citas.persona.model.Persona;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Recepcionista extends Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long indentificador;
}
