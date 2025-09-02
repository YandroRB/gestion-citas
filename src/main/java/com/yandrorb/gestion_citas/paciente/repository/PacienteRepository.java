package com.yandrorb.gestion_citas.paciente.repository;

import com.yandrorb.gestion_citas.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{
}
