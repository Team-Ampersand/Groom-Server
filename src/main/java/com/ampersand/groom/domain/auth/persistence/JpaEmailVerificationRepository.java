package com.ampersand.groom.domain.auth.persistence;

import com.ampersand.groom.domain.auth.persistence.adapter.email.SpringDataEmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaEmailVerificationRepository {

    private final SpringDataEmailVerificationRepository repository;


    public EmailVerification save(EmailVerification emailVerification) {
        return repository.save(emailVerification);
    }

    public Optional<EmailVerification> findByCode(String code) {
        return repository.findByCode(code);
    }

    public Optional<EmailVerification> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void deleteAllExpired(LocalDateTime now) {
        repository.deleteAllExpired(now);
    }

}