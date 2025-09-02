package com.yandrorb.gestion_citas.usuario.model;

import com.yandrorb.gestion_citas.usuario.model.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identificador;
    @Column(nullable = false , unique = true )
    private String username;
    @Setter
    private String password;

    @Column(nullable = false , unique = true )
    private String correo;

    @Enumerated(EnumType.STRING)
    private Roles rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(()->"ROLE_"+rol);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @PrePersist
    public void prePersist() {
        if(this.rol==null){
            this.rol= Roles.PACIENTE;
        }
    }

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() { return true;}
    @Override
    public boolean isAccountNonLocked() { return true;}
    @Override
    public boolean isCredentialsNonExpired() { return true;}
    @Override
    public boolean isEnabled() { return true;}

}
