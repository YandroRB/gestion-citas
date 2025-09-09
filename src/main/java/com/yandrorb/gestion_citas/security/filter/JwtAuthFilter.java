package com.yandrorb.gestion_citas.security.filter;

import com.yandrorb.gestion_citas.security.service.JWTService;
import com.yandrorb.gestion_citas.security.service.TokenBlackListService;
import com.yandrorb.gestion_citas.security.service.UserDetailServiceImp;
import com.yandrorb.gestion_citas.usuario.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private  JWTService jwtService;
    @Autowired
    private  UserDetailServiceImp userDetailServiceImp;
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TokenBlackListService tokenBlackListService;

    @Autowired
    public JwtAuthFilter (HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try{
            String authHeader = request.getHeader("Authorization");
            String jwt=null;
            String username=null;

            //Se verifica si el header en Authorization comienza con Bearer
            if(authHeader !=null&&authHeader.startsWith("Bearer ")){
                jwt = authHeader.substring(7);
                username=jwtService.extractUsername(jwt);
            }
            //Se verifica si encontró un nombre de usuario en el token y verifica si no hay ya un contexto de autenticacion
            if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
                if(!usuarioRepository.existsByUsername(username)){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
                if(tokenBlackListService.esTokenValido(jwt)){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
                UserDetails userDetails=userDetailServiceImp.loadUserByUsername(username);
                //Verifica si el token es valido y si es asi crea un contexto de autenticacion
                if(jwtService.isTokenValid(jwt,userDetails)){

                    UsernamePasswordAuthenticationToken authToken=
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            //En caso de error le delega al handlerExceptionResolver la excepcion
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
