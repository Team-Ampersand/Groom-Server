package com.ampersand.groom.domain.auth.application.service;

import com.ampersand.groom.domain.auth.application.port.EmailVerificationPort;
import com.ampersand.groom.domain.auth.domain.model.EmailVerification;
import com.ampersand.groom.domain.auth.expection.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationPort emailVerificationPort;
    private final JavaMailSender javaMailSender;


    //6자리 숫자 인증 코드 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 10000000 + random.nextInt(90000000);
        return String.valueOf(code);
    }

    // 이메일 전송 메서드
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    // 회원가입 인증 이메일 전송
    public void sendSignupVerificationEmail(String email) {
        String code = generateVerificationCode();
        sendEmail(email, "회원가입 인증", "귀하의 인증 코드는: " + code);

        EmailVerification emailVerification = new EmailVerification(email, code);
        emailVerificationPort.save(emailVerification);
    }

    // 비밀번호 변경을 위한 인증 이메일 전송
    public void sendPasswordResetEmail(String email) {
        String code = generateVerificationCode();
        sendEmail(email, "비밀번호 변경 인증", "귀하의 인증 코드는: " + code);

        EmailVerification emailVerification = new EmailVerification(email, code);
        emailVerificationPort.save(emailVerification);
    }

    // 인증 코드 검증
    public boolean verifyEmailCode(String code) {
        EmailVerification emailVerification = emailVerificationPort.findByCode(code)
                .orElseThrow(() -> new AuthException("Invalid or expired verification code"));

        emailVerification.setVerified(true);
        emailVerificationPort.save(emailVerification);
        return true;
    }

    // 만료된 인증 정보 삭제(1시간)
    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredVerifications() {
        emailVerificationPort.deleteAllExpired(LocalDateTime.now());
    }
}
