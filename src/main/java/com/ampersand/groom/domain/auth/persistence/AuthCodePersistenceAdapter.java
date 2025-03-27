package com.ampersand.groom.domain.auth.persistence;

import com.ampersand.groom.domain.auth.application.port.AuthCodePersistencePort;
import com.ampersand.groom.domain.auth.domain.AuthCode;
import com.ampersand.groom.domain.auth.persistence.mapper.AuthCodeMapper;
import com.ampersand.groom.domain.auth.persistence.repository.AuthCodeRedisRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;

@Adapter(AdapterType.OUTBOUND)
@RequiredArgsConstructor
public class AuthCodePersistenceAdapter implements AuthCodePersistencePort {

    // private final JpaEmailVerificationRepository jpaEmailVerificationRepository;
    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final AuthCodeMapper authCodeMapper;

    @Override
    public void saveAuthCode(AuthCode authCode) {
        authCodeRedisRepository.save(authCodeMapper.toEntity(authCode));
    }

    @Override
    public Boolean existsAuthCodeByCode(String code) {
        return authCodeRedisRepository.existsByCode(code);
    }

    @Override
    public AuthCode findAuthCodeByCode(String code) {
        return authCodeMapper.toDomain(authCodeRedisRepository.findByCode(code));
    }

    @Override
    public void deleteAuthCodeByCode(String code) {
        authCodeRedisRepository.deleteByCode(code);
    }
}