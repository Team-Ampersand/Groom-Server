package com.ampersand.groom.domain.auth.application.port;

import com.ampersand.groom.domain.auth.persistence.EmailVerification;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationPort {

    // 인증 정보 저장
    EmailVerification save(EmailVerification emailVerification);

    // 인증 코드로 이메일 조회
    Optional<EmailVerification> findByCode(String code);

    // 이메일로 인증 정보 조회
    Optional<EmailVerification> findByEmail(String email);

    // 만료된 인증 정보 삭제
    void deleteAllExpired(LocalDateTime now);
}