package com.ampersand.groom.domain.auth.persistence;

import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.exception.AuthenticationNotFoundException;
import com.ampersand.groom.domain.auth.persistence.mapper.AuthenticationMapper;
import com.ampersand.groom.domain.auth.persistence.repository.AuthenticationRedisRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;

@Adapter(AdapterType.OUTBOUND)
@RequiredArgsConstructor
public class AuthenticationPersistenceAdapter implements AuthenticationPersistencePort {

    private final AuthenticationRedisRepository authenticationRedisRepository;
    private final AuthenticationMapper authenticationMapper;

    @Override
    public Boolean existsAuthenticationByEmail(String email) {
        return authenticationRedisRepository.existsById(email);
    }

    @Override
    public Authentication findAuthenticationByEmail(String email) {
        return authenticationMapper.toDomain(authenticationRedisRepository.findById(email).orElseThrow(AuthenticationNotFoundException::new));
    }

    @Override
    public void saveAuthentication(Authentication authentication) {
        authenticationRedisRepository.save(authenticationMapper.toEntity(authentication));
    }
}