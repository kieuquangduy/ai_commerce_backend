package com.duy.aicommerce.backend.notification.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.duy.aicommerce.backend.auth.entity.VerificationToken;
import com.duy.aicommerce.backend.notification.template.VerificationEmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Data
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;




    @Async
    public void sendVerificationEmail(String email, String verifyLink) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); //cho phép gửi multipart

            VerificationEmailTemplate emailTemplate = new VerificationEmailTemplate();
            String html = emailTemplate.build(verifyLink);

            helper.setTo(email);
            helper.setSubject("Kích hoạt tài khoản của bạn");
            helper.setText(html, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            System.out.println(e.getMessage());

        }
    }
}
