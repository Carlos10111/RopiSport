package com.ropisport.gestion.security.jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ropisport.gestion.model.entity.Usuario;
import com.ropisport.gestion.util.Constants;

/**
 * Implementación de UserDetails para representar al usuario autenticado
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final String username;
    private final String email;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean enabled;

    private final String rol;

    public UserDetailsImpl(Integer id, String username, String email, String password,
                          Collection<? extends GrantedAuthority> authorities, boolean enabled, String rol) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.rol = rol;
    }

    /**
     * Construye un UserDetailsImpl a partir de un Usuario
     * @param usuario entidad Usuario
     * @return UserDetailsImpl
     */
    public static UserDetailsImpl build(Usuario usuario) {
        String roleName = usuario.getRol().getNombre();
        String fullRole = roleName.startsWith("ROLE_") ? roleName : Constants.ROLE_PREFIX + roleName;

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(fullRole));

        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPasswordHash(),
                authorities,
                usuario.getActivo(),
                usuario.getRol().getNombre()
        );
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}