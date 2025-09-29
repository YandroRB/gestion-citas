package com.yandrorb.gestion_citas.security.service;

import com.yandrorb.gestion_citas.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImp implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .map(u->{
                    if(!u.isEnabled()) throw new DisabledException("El usuario está desactivado");
                    return u;
                }).orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado "+username));
    }
}
