package com.ampersand.groom.domain.auth.application.service;

import com.ampersand.groom.domain.auth.application.port.EmailVerificationPort;
import com.ampersand.groom.domain.auth.expection.EmailFormatInvalidException;
import com.ampersand.groom.domain.auth.expection.VerificationCodeFormatInvalidException;
import com.ampersand.groom.domain.auth.expection.VerificationCodeExpiredOrInvalidException;
import com.ampersand.groom.domain.auth.persistence.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationPort emailVerificationPort;
    private final JavaMailSender javaMailSender;

    private static final int MAX_EMAIL_LENGTH = 16;
    private static final int CODE_LENGTH = 8;


    //8자리 숫자 인증 코드 생성
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
        verifyEmail(email);
        String code = generateVerificationCode();
        sendEmail(email, "회원가입 인증", "귀하의 인증 코드는: " + code);

        EmailVerification emailVerification = new EmailVerification(email, code);
        
        emailVerificationPort.save(emailVerification);
    }

    // 비밀번호 변경을 위한 인증 이메일 전송
    public void sendPasswordResetEmail(String email) {
        verifyEmail(email);
        String code = generateVerificationCode();
        sendEmail(email, "비밀번호 변경 인증", "귀하의 인증 코드는: " + code);

        EmailVerification emailVerification = new EmailVerification(email, code);
        
        emailVerificationPort.save(emailVerification);
    }

    // 인증 코드 검증
    public void verifyCode(String code) {
        if(code == null || code.length() != CODE_LENGTH) {
            throw new VerificationCodeFormatInvalidException();
        }

        EmailVerification emailVerification = emailVerificationPort.findByCode(code)
                .orElseThrow(VerificationCodeExpiredOrInvalidException::new);


        emailVerification.setIsVerified(true);
        emailVerificationPort.save(emailVerification);
    }

    // 이메일 검증
    public void verifyEmail(String email) {
        if(email == null || email.length() != MAX_EMAIL_LENGTH) {
            throw new EmailFormatInvalidException();
        }

    }
}
