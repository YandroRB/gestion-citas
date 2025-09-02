package com.yandrorb.gestion_citas.usuario.mapper;

import com.yandrorb.gestion_citas.usuario.Dto.response.UsuarioResponse;
import com.yandrorb.gestion_citas.usuario.model.Usuario;
import com.yandrorb.gestion_citas.utils.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UsuarioMapper extends GenericMapper<Usuario, UsuarioResponse> {
    UsuarioMapper INSTANCE= Mappers.getMapper(UsuarioMapper.class);
    @Override
    UsuarioResponse toDto(Usuario entity);

    @Override
    Usuario toEntity(UsuarioResponse dto);
}
