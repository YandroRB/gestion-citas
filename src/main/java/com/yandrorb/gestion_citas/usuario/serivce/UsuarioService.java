package com.yandrorb.gestion_citas.usuario.serivce;

import com.yandrorb.gestion_citas.exception.UsuarioExistente;
import com.yandrorb.gestion_citas.exception.ValidacionException;
import com.yandrorb.gestion_citas.security.Dto.request.UsuarioRequest;
import com.yandrorb.gestion_citas.security.service.LimitadorServices;
import com.yandrorb.gestion_citas.security.service.TokenBlackListService;
import com.yandrorb.gestion_citas.usuario.Dto.request.ActualizarUsuarioRequest;
import com.yandrorb.gestion_citas.usuario.model.Usuario;
import com.yandrorb.gestion_citas.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlackListService tokenBlackListService;
    private final LimitadorServices limitadorServices;

    public Map<String,Object> actualizarCredenciales(ActualizarUsuarioRequest request, String username){
        Usuario usuario= obtenerUsuario(username);
        if(!passwordEncoder.matches(request.getCurrentPassword(),usuario.getPassword())){
            throw new ValidacionException("La contraseña no coincide");
        }
        boolean invalidToken=false;
        if(request.getUsername()!=null){
            if(!limitadorServices.puedeCambiarUsuario(usuario.getIdentificador())){
                throw new ValidacionException("Has excedido el numero de cambios por semana, cambios maximo 3");
            }
            if(usuarioRepository.existsByUsername(request.getUsername())){
                throw new UsuarioExistente("El usuario que intenta registrar ya existe");
            }
            usuario.setUsername(request.getUsername());
            limitadorServices.guadarCambiosUsuario(usuario.getIdentificador());
            invalidToken=true;
        }
        if(request.getPassword()!=null){
            if(!limitadorServices.puedeCambiarPassword(usuario.getIdentificador())){
                throw new ValidacionException("Máximo 5 cambios por día (2 por hora)");
            }
            if(passwordEncoder.matches(request.getPassword(),usuario.getPassword())){
                throw new ValidacionException("Las contraseñas son iguales, ingrese una nueva contraseña");
            }
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            limitadorServices.guadarCambiosPassword(usuario.getIdentificador());
            invalidToken=true;
        }
        if(request.getCorreo()!=null){
            usuario.setCorreo(request.getCorreo());
        }
        if(invalidToken){
            tokenBlackListService.invalidarTokenUsuario(username);
        }
        usuarioRepository.save(usuario);
        Map<String,Object> map = new HashMap<>();
        map.put("message","Se ha actualizado exitosamente los datos");
        map.put("requiresReauth",invalidToken);
        return map;
    }

    private Usuario obtenerUsuario(String username){
        return usuarioRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("No se ha encontrado el usuario"));
    }

    public String desactivarUsuario(String username){
        Usuario usuario= obtenerUsuario(username);
        usuario.setEnabled(false);
        usuarioRepository.save(usuario);
        tokenBlackListService.invalidarTokenUsuario(username);
        return "Se ha desactivado el usuario";
    }

    public String activarUsuario(String username){
        Usuario usuario= obtenerUsuario(username);
        usuario.setEnabled(true);
        usuarioRepository.save(usuario);
        return "Se ha activado el usuario";
    }
}
