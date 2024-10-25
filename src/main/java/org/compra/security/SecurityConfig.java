package org.compra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfig {

    private final ExternalTokenAuthenticationFilter externalTokenAuthenticationFilter;

    public SecurityConfig(ExternalTokenAuthenticationFilter externalTokenAuthenticationFilter) {
        this.externalTokenAuthenticationFilter = externalTokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").hasAnyRole("USER", "ADMIN") // Ajusta según sea necesario
                        .anyRequest().authenticated()
                )
                .addFilterBefore(externalTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
