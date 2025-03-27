package com.ampersand.groom.infrastructure.thirdparty.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailSendService {

//        private val mailSender: JavaMailSender,
//    private val templateEngine: SpringTemplateEngine
//): NotificationSendPort {
//
//    companion object {
//        val EMAIL_SUBJECT = "GOMS 이메일 인증"
//    }
//
//    @Async
//    override fun sendNotification(email: String, authCode: String) {
//        val message = mailSender.createMimeMessage()
//        val helper = MimeMessageHelper(message, "utf-8")
//        helper.setTo(email)
//        helper.setSubject(EMAIL_SUBJECT)
//        val context = setContext(authCode)
//        helper.setText(context, true)
//
//        runCatching {
//            mailSender.send(message)
//        }.onFailure {
//            throw EmailSendFailException()
//        }
//    }
//
//    private fun setContext(authCode: String): String {
//        val template = "certificationMailTemplate"
//        val context = Context()
//
//        context.setVariable("authenticationCode", authCode)
//
//        return templateEngine.process(template, context)
//    }
//
//
//}

    private static final String EMAIL_SUBJECT = "Groom 이메일 인증";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(String to, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(EMAIL_SUBJECT);
        message.setText("귀하의 인증 코드는: " + authCode);
        mailSender.send(message);
    }
}
