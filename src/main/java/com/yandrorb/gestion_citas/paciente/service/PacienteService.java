package com.yandrorb.gestion_citas.paciente.service;

import com.yandrorb.gestion_citas.paciente.DTO.response.PacienteResponse;
import com.yandrorb.gestion_citas.paciente.mapper.PacienteMapper;
import com.yandrorb.gestion_citas.paciente.model.Paciente;
import com.yandrorb.gestion_citas.paciente.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;

    public PacienteResponse obtenerDatosPciente(String username){
        Paciente datos=obtenerPaciente(username);
        return PacienteMapper.INSTANCE.toDto(datos);
    }

    public Paciente obtenerPaciente(String username){
        return pacienteRepository.findByUsuarioUsername(username).orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));
    }
}
