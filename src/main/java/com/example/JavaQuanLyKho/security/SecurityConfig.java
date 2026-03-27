package com.example.JavaQuanLyKho.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @org.springframework.beans.factory.annotation.Value("${jwt.secret:}")
    private String jwtSecret;

    @org.springframework.beans.factory.annotation.Value("${jwt.expire:86400}")
    private String jwtExpire;

    @Bean
    public JwtService jwtService() {
        long expireSeconds;
        try {
            if (jwtExpire.endsWith("d")) {
                expireSeconds = Long.parseLong(jwtExpire.substring(0, jwtExpire.length() - 1)) * 24 * 3600;
            } else if (jwtExpire.endsWith("h")) {
                expireSeconds = Long.parseLong(jwtExpire.substring(0, jwtExpire.length() - 1)) * 3600;
            } else {
                expireSeconds = Long.parseLong(jwtExpire);
            }
        } catch (NumberFormatException e) {
            expireSeconds = 86400; // 1 day default
        }
        
        // Use default secret if not provided in .env
        String secret = (jwtSecret == null || jwtSecret.isEmpty()) 
            ? "defaultSecretKey_must_be_at_least_32_characters_long_for_HS256" 
            : jwtSecret;
            
        // Ensure secret is long enough for HS256 (32 bytes / 256 bits)
        if (secret.length() < 32) {
            secret = String.format("%-32s", secret).replace(' ', '0');
        }
        
        return new JwtService(secret, expireSeconds);
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
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService customUserDetailsService
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                writeApiError(
                                        response,
                                        HttpStatus.UNAUTHORIZED,
                                        "UNAUTHENTICATED",
                                        "Authentication required",
                                        request.getRequestURI()
                                );
                                return;
                            }
                            response.sendRedirect("/login");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                writeApiError(
                                        response,
                                        HttpStatus.FORBIDDEN,
                                        "FORBIDDEN",
                                        "You do not have permission to perform this action",
                                        request.getRequestURI()
                                );
                                return;
                            }
                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/warehouses/**").permitAll()
                        .requestMatchers("/uoms/**").permitAll()
                        .requestMatchers("/categories/**").permitAll()
                        .requestMatchers("/products/**").permitAll()
                        .requestMatchers("/api/v1/labels/**").permitAll()
                        .requestMatchers("/labels/**").permitAll()
                        .requestMatchers("/dispatch/**").permitAll()
                        .requestMatchers("/sections/**").permitAll()
                        // Users - Thymeleaf views
                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority("USER_VIEW")
                        .requestMatchers(HttpMethod.POST, "/users").hasAuthority("USER_CREATE")
                        .requestMatchers(HttpMethod.POST, "/users/{id}/update").hasAuthority("USER_UPDATE")
                        .requestMatchers(HttpMethod.POST, "/users/{id}/lock").hasAuthority("USER_LOCK")
                        .requestMatchers("/permissions/**").permitAll()
                        .requestMatchers("/login", "/logout").permitAll()
                        // Suppliers - Thymeleaf views
                        .requestMatchers(HttpMethod.GET, "/suppliers").hasAuthority("SUPPLIER_VIEW")
                        .requestMatchers(HttpMethod.POST, "/suppliers").hasAuthority("SUPPLIER_CREATE")
                        .requestMatchers(HttpMethod.POST, "/suppliers/{id}/update").hasAuthority("SUPPLIER_UPDATE")
                        .requestMatchers(HttpMethod.POST, "/suppliers/{id}/delete").hasAuthority("SUPPLIER_DELETE")
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
                        .requestMatchers(HttpMethod.GET, "/api/v1/inventory/balances").hasAnyAuthority("INVENTORY_CREATE", "INVENTORY_UPDATE", "INVENTORY_DELETE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/inventory/low-stock").hasAnyAuthority("INVENTORY_CREATE", "INVENTORY_UPDATE", "INVENTORY_DELETE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/inbounds/**").hasAnyAuthority("INBOUND_RECEIPT_CREATE", "INBOUND_RECEIPT_UPDATE", "INBOUND_RECEIPT_APPROVE", "INBOUND_RECEIPT_DELETE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/inbounds").hasAuthority("INBOUND_RECEIPT_CREATE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/inbounds/*/submit").hasAuthority("INBOUND_RECEIPT_UPDATE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/inbounds/*/approve").hasAuthority("INBOUND_RECEIPT_APPROVE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/inbounds/*/receive").hasAnyAuthority("INBOUND_RECEIPT_UPDATE", "INBOUND_RECEIPT_APPROVE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/outbounds/**").hasAnyAuthority("OUTBOUND_RECEIPT_CREATE", "OUTBOUND_RECEIPT_UPDATE", "OUTBOUND_RECEIPT_APPROVE", "OUTBOUND_RECEIPT_DELETE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/outbounds").hasAuthority("OUTBOUND_RECEIPT_CREATE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/outbounds/*/submit").hasAuthority("OUTBOUND_RECEIPT_UPDATE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/outbounds/*/approve").hasAuthority("OUTBOUND_RECEIPT_APPROVE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/outbounds/*/complete").hasAnyAuthority("OUTBOUND_RECEIPT_UPDATE", "OUTBOUND_RECEIPT_APPROVE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/transfers/**").hasAnyAuthority("OUTBOUND_RECEIPT_CREATE", "OUTBOUND_RECEIPT_UPDATE", "OUTBOUND_RECEIPT_APPROVE", "INBOUND_RECEIPT_CREATE", "INBOUND_RECEIPT_UPDATE", "INBOUND_RECEIPT_APPROVE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/transfers").hasAuthority("OUTBOUND_RECEIPT_CREATE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/transfers/*/approve").hasAuthority("OUTBOUND_RECEIPT_APPROVE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/transfers/*/issue").hasAuthority("OUTBOUND_RECEIPT_UPDATE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/transfers/*/receive").hasAuthority("INBOUND_RECEIPT_UPDATE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/suppliers/**").hasAuthority("SUPPLIER_VIEW")
                        .requestMatchers(HttpMethod.POST, "/api/v1/suppliers").hasAuthority("SUPPLIER_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/suppliers/**").hasAuthority("SUPPLIER_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/suppliers/**").hasAuthority("SUPPLIER_DELETE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/warehouses/*/sections/**").hasAuthority("LOCATION_VIEW")
                        .requestMatchers(HttpMethod.POST, "/api/v1/warehouses/*/sections/**").hasAuthority("LOCATION_CREATE")
                        // Green Warehouse - Thymeleaf views
                        .requestMatchers(HttpMethod.GET, "/green-warehouse/dashboard").hasAnyAuthority("GREEN_AGING_VIEW", "GREEN_SLOW_VIEW", "GREEN_EXPIRY_VIEW", "GREEN_WASTE_VIEW")
                        .requestMatchers(HttpMethod.GET, "/green-warehouse/aging").hasAuthority("GREEN_AGING_VIEW")
                        .requestMatchers(HttpMethod.GET, "/green-warehouse/slow-moving").hasAuthority("GREEN_SLOW_VIEW")
                        .requestMatchers(HttpMethod.GET, "/green-warehouse/expiring").hasAuthority("GREEN_EXPIRY_VIEW")
                        .requestMatchers(HttpMethod.GET, "/green-warehouse/waste").hasAuthority("GREEN_WASTE_VIEW")
                        // Green Warehouse - REST API
                        .requestMatchers(HttpMethod.GET, "/api/v1/green-warehouse/aging").hasAuthority("GREEN_AGING_VIEW")
                        .requestMatchers(HttpMethod.GET, "/api/v1/green-warehouse/slow-moving").hasAuthority("GREEN_SLOW_VIEW")
                        .requestMatchers(HttpMethod.GET, "/api/v1/green-warehouse/expiring").hasAuthority("GREEN_EXPIRY_VIEW")
                        .requestMatchers(HttpMethod.GET, "/api/v1/green-warehouse/waste/**").hasAuthority("GREEN_WASTE_VIEW")
                        .anyRequest().authenticated()
                )
                // Keep /logout for LoginViewController so audit log can be written before session invalidation.
                .logout(logout -> logout.logoutUrl("/perform-logout"))
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static void writeApiError(
            HttpServletResponse response,
            HttpStatus status,
            String code,
            String message,
            String path
    ) throws java.io.IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");

        Map<String, String> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", String.valueOf(status.value()));
        body.put("error", status.getReasonPhrase());
        body.put("code", code);
        body.put("message", message);
        body.put("path", path);

        String json = "{"
                + "\"timestamp\":\"" + escapeJson(body.get("timestamp")) + "\","
                + "\"status\":" + body.get("status") + ","
                + "\"error\":\"" + escapeJson(body.get("error")) + "\","
                + "\"code\":\"" + escapeJson(body.get("code")) + "\","
                + "\"message\":\"" + escapeJson(body.get("message")) + "\","
                + "\"path\":\"" + escapeJson(body.get("path")) + "\""
                + "}";
        response.getWriter().write(json);
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\\", "\\\\");
        escaped = escaped.replace("\"", "\\\"");
        escaped = escaped.replace("\n", "\\n");
        escaped = escaped.replace("\r", "\\r");
        return escaped.replace("\t", "\\t");
    }
}
