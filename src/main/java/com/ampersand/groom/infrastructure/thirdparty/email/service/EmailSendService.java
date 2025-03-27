package com.ampersand.groom.infrastructure.thirdparty.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailSendService {

    private static final String EMAIL_SUBJECT = "Groom 이메일 인증";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(String to, String authCode) throws MessagingException {
        Context context = new Context();
        context.setVariable("authCode", authCode);
        context.setVariable("logoCid", "groomLogo");
        String html = templateEngine.process("MailTemplate", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(EMAIL_SUBJECT);
        helper.setText(html, true);
        FileSystemResource logoImage = new FileSystemResource(
                new File("src/main/resources/static/images/groom-logo.png")
        );
        helper.addInline("groomLogo", logoImage);
        mailSender.send(message);
    }
}