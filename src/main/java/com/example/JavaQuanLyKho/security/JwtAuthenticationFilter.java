package com.example.JavaQuanLyKho.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = null;

        // 1. Ưu tiên đọc từ Authorization header (REST API)
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        // 2. Fallback: đọc từ Session (Thymeleaf / browser)
        if (token == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object sessionToken = session.getAttribute("AUTH_TOKEN");
                if (sessionToken instanceof String) {
                    token = (String) sessionToken;
                }
            }
        }

        if (token != null) {
            try {
                Claims claims = jwtService.parseToken(token);
                String username = claims.getSubject();
                Object authoritiesClaim = claims.get("authorities");
                Collection<? extends GrantedAuthority> authorities = mapAuthorities(authoritiesClaim);
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(Object authoritiesClaim) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (authoritiesClaim instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof String value) {
                    authorities.add(new SimpleGrantedAuthority(value));
                }
            }
        }
        return authorities;
    }
}

