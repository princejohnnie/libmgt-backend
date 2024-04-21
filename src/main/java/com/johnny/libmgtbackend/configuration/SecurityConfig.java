package com.johnny.libmgtbackend.configuration;

import com.johnny.libmgtbackend.auth.CustomAuthenticationFilter;
import com.johnny.libmgtbackend.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((authorization) -> authorization
                        .requestMatchers(HttpMethod.POST, "/api/register", "/api/login")
                        .permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/**")
                        .authenticated()
                        .anyRequest()
                        .permitAll()
                ).csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new CustomAuthenticationFilter(new TokenService()), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
