package com.ubbackend.servicesImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


@Service
public class MailSenderImpl {

    private final JavaMailSender mailSender;

    public MailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String text, String url, Charset utf8) throws MessagingException, IOException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom(new InternetAddress("cursosaub@gmail.com"));
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
        mimeMessage.setSubject(subject);

        ClassPathResource resource = new ClassPathResource("static/index.html");
        try (InputStream in = resource.getInputStream()) {
            String html = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            StringBuilder htmlBuilder = new StringBuilder();

            for(char c : html.toCharArray()) {
                htmlBuilder.append(c);
            }

            String htmlString = htmlBuilder.toString().replace("%ACCESS_CODE%", text).replace("%URL%", url);
            mimeMessage.setContent(htmlString, "text/html; charset=utf-8");
            mailSender.send(mimeMessage);
        }
    }
}
