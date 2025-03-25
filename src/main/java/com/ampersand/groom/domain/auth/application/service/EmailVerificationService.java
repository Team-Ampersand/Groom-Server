package com.ampersand.groom.domain.auth.application.service;

import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.application.port.EmailVerificationPort;
import com.ampersand.groom.domain.auth.domain.AuthCode;
import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.exception.EmailAuthRateLimitException;
import com.ampersand.groom.domain.auth.exception.EmailFormatInvalidException;
import com.ampersand.groom.domain.auth.exception.VerificationCodeFormatInvalidException;
import com.ampersand.groom.domain.auth.exception.VerificationCodeExpiredOrInvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final EmailVerificationPort emailVerificationPort;
    private final JavaMailSender javaMailSender;

    private static final int MAX_EMAIL_LENGTH = 16;
    private static final int CODE_LENGTH = 8;
    private static final int MAX_ATTEMPT_COUNT = 5;
    private static final long TTL = 300L;

    // 인증 코드 생성
    private String generateVerificationCode() {
        int code = 10000000 + new Random().nextInt(90000000);
        return String.valueOf(code);
    }

    // 이메일 전송
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    // 인증 이메일 전송 (회원가입 / 비밀번호 재설정 공통 처리)
    private void sendVerificationEmail(String email, String subject) {
        validateEmailFormat(email);
        checkAttemptCount(email);
        increaseAttemptCount(email);

        String code = generateVerificationCode();
        sendEmail(email, subject, "귀하의 인증 코드는: " + code);

        AuthCode authCode = AuthCode.builder()
                .email(email)
                .code(code)
                .ttl(TTL)
                .build();

        emailVerificationPort.saveAuthCode(authCode);
    }

    // 회원가입용 인증 메일 전송
    public void sendSignupVerificationEmail(String email) {
        sendVerificationEmail(email, "회원가입 인증");
    }

    // 비밀번호 재설정 인증 메일 전송
    public void sendPasswordResetEmail(String email) {
        sendVerificationEmail(email, "비밀번호 변경 인증");
    }

    // 인증 코드 검증
    public void verifyCode(String code) {
        if (code == null || code.length() != CODE_LENGTH) {
            throw new VerificationCodeFormatInvalidException();
        }

        if (!emailVerificationPort.existsAuthCodeByCode(code)) {
            throw new VerificationCodeExpiredOrInvalidException();
        }

        AuthCode authCode = emailVerificationPort.findAuthCodeByCode(code);
        emailVerificationPort.deleteAuthCodeByCode(code);
        markEmailVerified(authCode.getEmail());
    }

    // 이메일 형식 검증
    private void validateEmailFormat(String email) {
        if (email == null || email.length() != MAX_EMAIL_LENGTH) {
            throw new EmailFormatInvalidException();
        }
    }

    // 인증 시도 횟수 초과 체크
    private void checkAttemptCount(String email) {
        if (authenticationPersistencePort.existsAuthenticationByEmail(email)) {
            int attempts = authenticationPersistencePort.findAuthenticationByEmail(email).getAttemptCount();
            if (attempts >= MAX_ATTEMPT_COUNT) {
                throw new EmailAuthRateLimitException();
            }
        }
    }

    // 인증 시도 횟수 증가
    private void increaseAttemptCount(String email) {
        Authentication existingAuth = authenticationPersistencePort.existsAuthenticationByEmail(email)
                ? authenticationPersistencePort.findAuthenticationByEmail(email)
                : null;
        Authentication updatedAuth;
        if(existingAuth != null) {
            updatedAuth = Authentication.builder()
                    .email(email)
                    .attemptCount(existingAuth.getAttemptCount() + 1)
                    .verified(existingAuth.getVerified())
                    .ttl(existingAuth.getTtl())
                    .build();
        } else {
            updatedAuth = Authentication.builder()
                    .email(email)
                    .attemptCount(1)
                    .verified(false)
                    .ttl(TTL)
                    .build();
        }
        authenticationPersistencePort.saveAuthentication(updatedAuth);
    }

    // 인증 완료 처리
    private void markEmailVerified(String email) {
        Authentication authentication = authenticationPersistencePort.findAuthenticationByEmail(email);

        Authentication updatedAuth = Authentication.builder()
                .email(email)
                .attemptCount(authentication.getAttemptCount())
                .verified(true)
                .ttl(authentication.getTtl())
                .build();

        authenticationPersistencePort.saveAuthentication(updatedAuth);
    }
}