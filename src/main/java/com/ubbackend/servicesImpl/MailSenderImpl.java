package com.ubbackend.servicesImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import static org.antlr.v4.runtime.misc.Utils.readFile;

@Service
public class MailSenderImpl {

    private final JavaMailSender mailSender;

    public MailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String text, String url) throws MessagingException, IOException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom(new InternetAddress("cursosaub@gmail.com"));
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
        mimeMessage.setSubject(subject);

        char[] htmlTemplate = readFile("src/main/resources/static/index.html");
        StringBuilder html = new StringBuilder();

        for(char c : htmlTemplate) {
            html.append(c);
        }

        String htmlString = html.toString().replace("%ACCESS_CODE%", text).replace("%URL%", url);


        mimeMessage.setContent(htmlString, "text/html; charset=utf-8");
        mailSender.send(mimeMessage);
    }
}
