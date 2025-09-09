package com.yandrorb.gestion_citas.paciente.repository;

import com.yandrorb.gestion_citas.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    Optional<Paciente> findByUsuarioUsername(String usuario);
}
