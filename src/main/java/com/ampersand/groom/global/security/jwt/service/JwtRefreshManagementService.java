package com.ampersand.groom.global.security.jwt.service;

public interface JwtRefreshManagementService {
    void deleteRefreshToken(String token);
}