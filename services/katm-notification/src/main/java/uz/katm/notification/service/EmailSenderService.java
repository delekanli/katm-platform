package uz.katm.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uz.katm.notification.exception.EmailException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(String to, String subject, String body) {
        log.debug("Sending email: to={}, subject={}", to, subject);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            log.info("Email sent: to={}", to);
        } catch (MessagingException e) {
            log.error("Email send error: to={}, error={}", to, e.getMessage(), e);
            throw new EmailException("Ошибка отправки email: " + e.getMessage(), e);
        }
    }

    public void sendPlainEmail(String to, String subject, String body) {
        log.debug("Sending plain email: to={}, subject={}", to, subject);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            mailSender.send(message);
            log.info("Plain email sent: to={}", to);
        } catch (MessagingException e) {
            log.error("Plain email send error: to={}, error={}", to, e.getMessage(), e);
            throw new EmailException("Ошибка отправки email: " + e.getMessage(), e);
        }
    }
}
