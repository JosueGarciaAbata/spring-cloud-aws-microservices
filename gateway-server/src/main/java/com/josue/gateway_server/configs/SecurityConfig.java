package com.josue.gateway_server.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(authz ->
                        authz
                        .pathMatchers("/authorized", "/logout").permitAll()
                        .pathMatchers("/api/v1/items/**", "/api/v1/users/**", "/api/v1/products/**").hasAnyRole("ADMIN", "USER")
                        .anyExchange().authenticated()
                ).cors(ServerHttpSecurity.CorsSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwtSpec -> jwtSpec.jwtAuthenticationConverter(
                                (Converter<Jwt, Mono<AbstractAuthenticationToken>>) source -> {
                                    Collection<String> roles = source.getClaimAsStringList("roles");
                                    Collection<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                                    return Mono.just(new JwtAuthenticationToken(source, authorities));
                                }
                        )
                ))
                .build();
    }
}
