package com.example.JavaQuanLyKho.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public JwtService jwtService() {
        return new JwtService(3600);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService customUserDetailsService) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/warehouses/**").permitAll()
                        .requestMatchers("/uoms/**").permitAll()
                        .requestMatchers("/categories/**").permitAll()
                        .requestMatchers("/products/**").permitAll()
                        .requestMatchers("/dispatch/**").permitAll()
                        .requestMatchers("/suppliers/**").permitAll()
                        .requestMatchers("/locations/**").permitAll()
                        // Users - Thymeleaf views
                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority("USER_VIEW")
                        .requestMatchers(HttpMethod.POST, "/users").hasAuthority("USER_CREATE")
                        .requestMatchers(HttpMethod.POST, "/users/{id}/update").hasAuthority("USER_UPDATE")
                        .requestMatchers(HttpMethod.POST, "/users/{id}/lock").hasAuthority("USER_LOCK")
                        .requestMatchers("/permissions/**").permitAll()
                        .requestMatchers("/login", "/logout").permitAll()
                        // Roles - Thymeleaf views
                        .requestMatchers(HttpMethod.GET, "/roles").hasAuthority("ROLE_VIEW")
                        .requestMatchers(HttpMethod.POST, "/roles").hasAuthority("ROLE_CREATE")
                        .requestMatchers(HttpMethod.POST, "/roles/assign-permissions").hasAuthority("ROLE_UPDATE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/uoms/**").hasAuthority("UOM_VIEW")
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").hasAuthority("CATEGORY_VIEW")
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").hasAuthority("PRODUCT_VIEW")
                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasAuthority("PRODUCT_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAuthority("PRODUCT_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAuthority("PRODUCT_DELETE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/*/lock").hasAuthority("PRODUCT_LOCK")
                        .requestMatchers(HttpMethod.GET, "/api/v1/warehouses/*/locations/**").hasAuthority("LOCATION_VIEW")
                        .requestMatchers(HttpMethod.POST, "/api/v1/warehouses/*/locations/**").hasAuthority("LOCATION_CREATE")
                        .anyRequest().authenticated()
                )
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
