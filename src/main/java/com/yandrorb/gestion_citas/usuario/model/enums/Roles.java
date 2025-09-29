package com.yandrorb.gestion_citas.usuario.model.enums;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Roles {
    DENTISTA("ROLE_DENTISTA",Set.of()),
    RECEPCIONISTA("ROLE_RECEPCIONISTA",Set.of("ver_datos_paciente","editar_datos_paciente")),
    PACIENTE("ROLE_PACIENTE",Set.of("ver_datos_paciente","editar_datos_paciente")),;

    private final String stringRole;
    private final Set<String> permisos;
    private Roles(String stringRole, Set<String> permisos) {
        this.stringRole = stringRole;
        this.permisos = permisos;
    }
}
