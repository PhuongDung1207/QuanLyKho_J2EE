package com.example.JavaQuanLyKho.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

public class JwtService {

    private final SecretKey secretKey;

    private final long accessTokenTtlSeconds;

    public JwtService(long accessTokenTtlSeconds) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.accessTokenTtlSeconds = accessTokenTtlSeconds;
    }

    public String generateToken(String username, Set<String> authorities) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(accessTokenTtlSeconds);
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

