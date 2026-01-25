package com.Linko.services.impl;

import com.Linko.entities.Email;
import com.Linko.services.EmailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // VERIFIED sender
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);
    }

    @Override
    public void sendEmailToContact(
            Email email) {

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // SMTP sender (must be your app email)
            helper.setFrom(new InternetAddress(from, "Linko"));

            // Recipient (contact)
            helper.setTo(email.getTo());

            // Reply goes to user, not Linko
            helper.setReplyTo(email.getFrom());

            // Subject
            // helper.setSubject(
            // "You have been contacted by " + email.getName() + " via Linko");
            helper.setSubject(email.getSubject());

            String formattedContent = email.getContent()
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\n", "<br>");

            // Email Body
            String body = """
                    <p>Hello,</p>

                    <p><strong>%s</strong> has contacted you via <strong>Linko</strong>.</p>

                    <p><strong>Message:</strong></p>
                    <p>%s</p>

                    <hr>
                    <p>
                        <strong>Sender Email:</strong> %s<br>
                        Sent using Linko Smart Manager
                    </p>
                    """.formatted(
                    email.getName(),
                    formattedContent,
                    email.getFrom()
            );


            helper.setText(body, true);

            emailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendEmailWithHtml() {

        throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithHtml'");
    }

    @Override
    public void sendEmailWithAttachment() {

        throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithAttachment'");
    }

}