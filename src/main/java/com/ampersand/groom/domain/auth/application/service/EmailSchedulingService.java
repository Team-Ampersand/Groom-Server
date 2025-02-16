package com.ampersand.groom.domain.auth.application.service;

import com.ampersand.groom.domain.auth.application.port.EmailVerificationPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailSchedulingService {

    private final EmailVerificationPort emailVerificationPort;

    // 만료된 인증 정보 삭제(1시간)
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deleteExpiredVerifications() {
        emailVerificationPort.deleteAllExpired(LocalDateTime.now());
    }
}
