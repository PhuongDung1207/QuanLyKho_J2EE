package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.dto.AuthDtos;
import com.example.JavaQuanLyKho.model.entity.AuditLog;
import com.example.JavaQuanLyKho.model.entity.User;
import com.example.JavaQuanLyKho.repository.AuditLogRepository;
import com.example.JavaQuanLyKho.repository.UserRepository;
import com.example.JavaQuanLyKho.security.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements com.example.JavaQuanLyKho.service.AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final AuditLogRepository auditLogRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public AuthDtos.LoginResponse login(AuthDtos.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        String token = jwtService.generateToken(request.getUsername(), authorities);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        AuditLog auditLog = new AuditLog();
        auditLog.setActorUserId(user.getId());
        auditLog.setAction("LOGIN");
        auditLog.setEntityType("User");
        auditLog.setEntityId(user.getId());
        auditLog.setCreatedAt(OffsetDateTime.now());
        auditLogRepository.save(auditLog);
        AuthDtos.LoginResponse response = new AuthDtos.LoginResponse();
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setExpiresIn(3600);
        return response;
    }

    @Override
    public AuthDtos.LoginResponse refresh(String token) {
        Claims claims = jwtService.parseToken(token);
        String username = claims.getSubject();
        Object authoritiesClaim = claims.get("authorities");
        Set<String> authorities = JwtAuthoritiesExtractor.extract(authoritiesClaim);
        String newToken = jwtService.generateToken(username, authorities);
        AuthDtos.LoginResponse response = new AuthDtos.LoginResponse();
        response.setAccessToken(newToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(3600);
        return response;
    }

    private static class JwtAuthoritiesExtractor {

        static Set<String> extract(Object claim) {
            if (claim instanceof java.util.List<?> list) {
                return list.stream()
                        .filter(item -> item instanceof String)
                        .map(item -> (String) item)
                        .collect(Collectors.toSet());
            }
            return java.util.Collections.emptySet();
        }
    }
}

