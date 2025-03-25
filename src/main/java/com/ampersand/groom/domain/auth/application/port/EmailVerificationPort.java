package com.ampersand.groom.domain.auth.application.port;

import com.ampersand.groom.domain.auth.domain.AuthCode;

public interface EmailVerificationPort {

    // 코드로 인증 코드 존재 여부 조회
    Boolean existsAuthCodeByCode(String code);

    // 코드로 인증 코드 조회
    AuthCode findAuthCodeByCode(String code);

    // 인증 코드 저장
    void saveAuthCode(AuthCode authCode);

    // 인증 코드 삭제
    void deleteAuthCodeByCode(String code);
}