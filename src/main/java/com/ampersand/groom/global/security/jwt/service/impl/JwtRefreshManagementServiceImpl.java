package com.ampersand.groom.global.security.jwt.service.impl;

import com.ampersand.groom.global.security.jwt.repository.RefreshTokenRedisRepository;
import com.ampersand.groom.global.security.jwt.service.JwtRefreshManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtRefreshManagementServiceImpl implements JwtRefreshManagementService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRedisRepository.deleteById(token);
    }
}