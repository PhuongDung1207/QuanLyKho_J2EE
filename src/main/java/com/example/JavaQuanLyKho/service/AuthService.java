package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.dto.AuthDtos;

public interface AuthService {

    AuthDtos.LoginResponse login(AuthDtos.LoginRequest request);

    AuthDtos.LoginResponse refresh(String token);
}

