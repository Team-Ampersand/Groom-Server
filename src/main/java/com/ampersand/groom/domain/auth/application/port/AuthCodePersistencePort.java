package com.ampersand.groom.domain.auth.application.port;

import com.ampersand.groom.domain.auth.domain.AuthCode;

public interface AuthCodePersistencePort {
    Boolean existsAuthCodeByCode(String code);

    AuthCode findAuthCodeByCode(String code);

    void saveAuthCode(AuthCode authCode);

    void deleteAuthCodeByCode(String code);
}