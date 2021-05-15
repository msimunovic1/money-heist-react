package hr.msimunovic.moneyheist.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${email.notification.sender}")
    private String from;

    @Override
    public void sendEmail(String to, String subject, String text) {

        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            messageHelper.setSubject(subject);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setText("<html><body><p>" + text + "</body></html>", true);

        } catch (MessagingException e) {
            log.error(e.getMessage());
        }

        emailSender.send(mimeMessage);

    }
}
