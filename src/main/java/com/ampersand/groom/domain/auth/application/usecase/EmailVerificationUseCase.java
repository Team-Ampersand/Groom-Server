package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.service.EmailVerificationService;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationUseCase {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationUseCase(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    // 회원가입 인증 이메일 전송
    public void executeSendSignupVerificationEmail(String email) {
        emailVerificationService.sendSignupVerificationEmail(email);
    }

    // 비밀번호 변경 인증 이메일 전송
    public void executeSendPasswordResetEmail(String email) {
        emailVerificationService.sendPasswordResetEmail(email);
    }

    // 이메일 인증 코드 검증
    public boolean executeVerifyEmail(String code) {
        return emailVerificationService.verifyEmailCode(code);
    }
}
