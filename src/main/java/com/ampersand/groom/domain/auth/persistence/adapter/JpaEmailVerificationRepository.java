package com.ampersand.groom.domain.auth.persistence.adapter;

import com.ampersand.groom.domain.auth.application.port.EmailVerificationPort;
import com.ampersand.groom.domain.auth.domain.model.EmailVerification;
import com.ampersand.groom.domain.auth.persistence.adapter.email.SpringDataEmailVerificationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class JpaEmailVerificationRepository implements EmailVerificationPort {

    private final SpringDataEmailVerificationRepository repository;

    public JpaEmailVerificationRepository(SpringDataEmailVerificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return repository.save(emailVerification);
    }

    @Override
    public Optional<EmailVerification> findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Optional<EmailVerification> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void deleteAllExpired(LocalDateTime now) {
        repository.deleteAllExpired(now);
    }

}