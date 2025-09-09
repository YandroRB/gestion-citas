package com.yandrorb.gestion_citas.paciente.service;

import com.yandrorb.gestion_citas.exception.ValidacionException;
import com.yandrorb.gestion_citas.paciente.DTO.request.ActualizarPacienteRequest;
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
    public PacienteResponse actualizarDatosPaciente(String username, ActualizarPacienteRequest request){
        if(!request.tieneCampos()){
            throw new ValidacionException("Debe proporcionar al menos un campo para actualizar");
        }
        Paciente datos=obtenerPaciente(username);
        if(request.getNombres() !=null){
            datos.setNombres(request.getNombres());
        }
        if(request.getApellidos() !=null){
            datos.setApellidos(request.getApellidos());
        }
        if(request.getCedula()!=null){
            datos.setCedula(request.getCedula());
        }
        if(request.getNum_celular()!=null){
            datos.setNum_celular(request.getNum_celular());
        }
        Paciente datosActualizados=pacienteRepository.save(datos);
        return PacienteMapper.INSTANCE.toDto(datosActualizados);
    }

    private Paciente obtenerPaciente(String username){
        return pacienteRepository.findByUsuarioUsername(username).orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));
    }
}
