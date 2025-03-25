package com.ampersand.groom.domain.auth.persistence.mapper;

import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.persistence.entity.AuthenticationRedisEntity;
import com.ampersand.groom.global.mapper.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper implements GenericMapper<AuthenticationRedisEntity, Authentication> {

    @Override
    public Authentication toDomain(AuthenticationRedisEntity authenticationRedisEntity) {
        return Authentication.builder()
                .email(authenticationRedisEntity.getEmail())
                .attemptCount(authenticationRedisEntity.getAttemptCount())
                .verified(authenticationRedisEntity.getVerified())
                .ttl(authenticationRedisEntity.getTtl())
                .build();
    }

    @Override
    public AuthenticationRedisEntity toEntity(Authentication authentication) {
        return AuthenticationRedisEntity.builder()
                .email(authentication.getEmail())
                .attemptCount(authentication.getAttemptCount())
                .verified(authentication.getVerified())
                .ttl(authentication.getTtl())
                .build();
    }
}