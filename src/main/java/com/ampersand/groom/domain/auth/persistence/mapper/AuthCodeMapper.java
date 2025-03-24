package com.ampersand.groom.domain.auth.persistence.mapper;

import com.ampersand.groom.domain.auth.domain.AuthCode;
import com.ampersand.groom.domain.auth.persistence.entity.AuthCodeRedisEntity;
import com.ampersand.groom.global.mapper.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthCodeMapper implements GenericMapper<AuthCodeRedisEntity, AuthCode> {

    @Override
    public AuthCode toDomain(AuthCodeRedisEntity authCodeRedisEntity) {
        return AuthCode.builder()
                .email(authCodeRedisEntity.getEmail())
                .code(authCodeRedisEntity.getCode())
                .ttl(authCodeRedisEntity.getTtl())
                .build();
    }

    @Override
    public AuthCodeRedisEntity toEntity(AuthCode authCode) {
        return AuthCodeRedisEntity.builder()
                .email(authCode.getEmail())
                .code(authCode.getCode())
                .ttl(authCode.getTtl())
                .build();
    }
}