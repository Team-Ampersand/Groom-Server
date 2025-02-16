package com.ampersand.groom.domain.auth.persistence.adapter.email;

import com.ampersand.groom.domain.auth.application.port.EmailVerificationPort;

import com.ampersand.groom.domain.auth.persistence.EmailVerification;
import com.ampersand.groom.domain.auth.persistence.JpaEmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailVerificationAdapter implements EmailVerificationPort {

    private final JpaEmailVerificationRepository jpaEmailVerificationRepository;

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return jpaEmailVerificationRepository.save(emailVerification);
    }

    @Override
    public Optional<EmailVerification> findByCode(String code) {
        return jpaEmailVerificationRepository.findByCode(code);
    }

    @Override
    public Optional<EmailVerification> findByEmail(String email) {
        return jpaEmailVerificationRepository.findByEmail(email);
    }

    @Override
    public void deleteAllExpired(LocalDateTime now) {
        jpaEmailVerificationRepository.deleteAllExpired(now);
    }
}
