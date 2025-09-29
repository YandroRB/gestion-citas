package com.yandrorb.gestion_citas.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

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


    private final ObjectMapper objectMapper=new ObjectMapper();

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
                    sendResponse(response,HttpStatus.UNAUTHORIZED,"usuario no encontrado");
                    return;
                }

                if(tokenBlackListService.esTokenValido(jwt)){
                    sendResponse(response,HttpStatus.UNAUTHORIZED,"Token no valido debido a cambios de credenciales");
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
        }
        catch (Exception ex) {
            //En caso de error le delega al handlerExceptionResolver la excepcion
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }

    private void sendResponse(HttpServletResponse response,HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String,Object> errorResponse=new HashMap<>();
        errorResponse.put("error",message);
        errorResponse.put("status",status.value());
        errorResponse.put("timestamp",System.currentTimeMillis());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
