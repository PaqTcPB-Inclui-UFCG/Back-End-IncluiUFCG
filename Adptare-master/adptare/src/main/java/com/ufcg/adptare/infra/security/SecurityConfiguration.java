package com.ufcg.adptare.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(autorize -> autorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/articles/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/{userId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/{userId}/photo").authenticated()
                        .requestMatchers(HttpMethod.GET,
                                "/users/userByEmail={email}")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/attachments/download").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/attachments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/articles/**").permitAll()

                        // novos metodos
                        .requestMatchers(HttpMethod.PUT, "/api/articles/{idArticle}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/articles/{idArticle}/like").permitAll()
                        // novos metodos

                        .requestMatchers(HttpMethod.GET, "/api/articles/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/attachments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .anyRequest().authenticated()

                )
                .addFilterBefore(this.securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
