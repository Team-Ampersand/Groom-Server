package com.ampersand.groom.domain.auth.persistence.repository;

import com.ampersand.groom.domain.auth.persistence.EmailVerification;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SpringDataEmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByCode(String code);

    Optional<EmailVerification> findByEmail(String email);

    @Modifying
    @Query("DELETE FROM EmailVerification e WHERE e.verificationDate < :now")
    void deleteAllExpired(@Param("now") LocalDateTime now);
}
