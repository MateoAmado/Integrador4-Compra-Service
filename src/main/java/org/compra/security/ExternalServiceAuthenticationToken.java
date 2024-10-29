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
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
}
