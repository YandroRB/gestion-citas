package com.yandrorb.gestion_citas.paciente.mapper;

import com.yandrorb.gestion_citas.paciente.DTO.response.PacienteResponse;
import com.yandrorb.gestion_citas.paciente.model.Paciente;
import com.yandrorb.gestion_citas.usuario.mapper.UsuarioMapper;
import com.yandrorb.gestion_citas.utils.GenericMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = UsuarioMapper.class)
public interface PacienteMapper extends GenericMapper<Paciente, PacienteResponse> {
    PacienteMapper INSTANCE= Mappers.getMapper(PacienteMapper.class);

    @Override
    @InheritInverseConfiguration
    PacienteResponse toDto(Paciente entity);

    @Override
    Paciente toEntity(PacienteResponse dto);
}
