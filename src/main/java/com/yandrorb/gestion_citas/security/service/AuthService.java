package com.yandrorb.gestion_citas.security.service;

import com.yandrorb.gestion_citas.exception.UsuarioExistente;
import com.yandrorb.gestion_citas.paciente.model.Paciente;
import com.yandrorb.gestion_citas.paciente.repository.PacienteRepository;
import com.yandrorb.gestion_citas.security.Dto.request.AuthRequest;
import com.yandrorb.gestion_citas.security.Dto.request.RegistroRequest;
import com.yandrorb.gestion_citas.security.Dto.response.AuthResponse;
import com.yandrorb.gestion_citas.usuario.model.Usuario;
import com.yandrorb.gestion_citas.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public Paciente registroUsuario(RegistroRequest registroRequest) {
        try{
            validarUsuario(registroRequest.getUsuario().getUsername(), registroRequest.getUsuario().getCorreo());

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setCorreo(registroRequest.getUsuario().getCorreo());
            nuevoUsuario.setPassword(passwordEncoder.encode(registroRequest.getUsuario().getPassword()));
            nuevoUsuario.setUsername(registroRequest.getUsuario().getUsername());
            Usuario usuarioGuardado=usuarioRepository.save(nuevoUsuario);

            Paciente nuevoPaciente = new Paciente();
            nuevoPaciente.setUsuario(usuarioGuardado);
            nuevoPaciente.setNombres(registroRequest.getNombre());
            nuevoPaciente.setApellidos(registroRequest.getApellido());
            nuevoPaciente.setCedula(registroRequest.getCedula());
            nuevoPaciente.setNum_celular(registroRequest.getNum_celular());

            return pacienteRepository.save(nuevoPaciente);
        }catch(DataIntegrityViolationException ex){
            throw new UsuarioExistente(ex.getMessage());
        }
    }
    public AuthResponse loginUsuario(AuthRequest authRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            /*
            * Es lo mismo que hacer UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            * Solo que aqui valida que el authentication.getPrincipal() sea de tipo UserDetails
            * y si lo es la instancia estará disponible
            * */
            if(!(authentication.getPrincipal() instanceof UserDetails userDetails)){
                throw new IllegalStateException("No es UserDetails");
            }
            Map<String,Object> extraClaims= new HashMap<>();
            String token= jwtService.generateToken(extraClaims,userDetails);
            String refreshToken= jwtService.generateRefreshToken(userDetails);
            return AuthResponse.builder().username(userDetails.getUsername()).token(token).refreshToken(refreshToken).build();

        }catch (BadCredentialsException ex){
            throw new UsernameNotFoundException("Credenciales invalidas");
        }
    }
    private void validarUsuario(String username,String email) {
        if(usuarioRepository.existsByUsername(username)){
            throw new UsuarioExistente("El nombre de usuario "+username+" ya existe");
        }
        if(usuarioRepository.existsByCorreo(email)){
            throw new UsuarioExistente("El correo "+email+" ya se encuentra registrado con otro usuario");
        }
    }
}
