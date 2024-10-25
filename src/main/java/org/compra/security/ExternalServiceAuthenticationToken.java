package org.compra.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class ExternalServiceAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final Collection<GrantedAuthority> authorities;

    public ExternalServiceAuthenticationToken(String token, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.authorities = authorities;
        setAuthenticated(true);  // Este token ahora está autenticado
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return null; // Aquí podrías devolver el usuario autenticado si lo deseas
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
}
