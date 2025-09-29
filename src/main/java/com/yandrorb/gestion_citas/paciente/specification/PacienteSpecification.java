package com.yandrorb.gestion_citas.paciente.specification;

import com.yandrorb.gestion_citas.paciente.DTO.request.BusquedaPacienteRequest;
import com.yandrorb.gestion_citas.paciente.model.Paciente;
import com.yandrorb.gestion_citas.persona.model.Persona;
import com.yandrorb.gestion_citas.usuario.model.Usuario;
import io.netty.util.internal.StringUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

public class PacienteSpecification {
    public static Specification<Paciente> criterios(BusquedaPacienteRequest criterio){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(criterio.getCedula())){
                predicates.add(criteriaBuilder.equal(root.get("cedula"), criterio.getCedula()));
            }
            if(StringUtils.hasText(criterio.getUsername())){
                Join<Persona, Usuario> usuarioJoin= root.join("usuario");
                predicates.add(criteriaBuilder.equal(usuarioJoin.get("username"), criterio.getUsername()));
            }
            if(StringUtils.hasText(criterio.getNombre())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nombres")),
                        "%"+criterio.getNombre().toLowerCase()+"%"));
            }
            if(StringUtils.hasText(criterio.getApellido())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("apellidos")),
                        "%"+criterio.getApellido().toLowerCase()+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
