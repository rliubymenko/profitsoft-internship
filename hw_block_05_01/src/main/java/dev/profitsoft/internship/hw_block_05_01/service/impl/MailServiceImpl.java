package dev.profitsoft.internship.hw_block_05_01.service.impl;

import dev.profitsoft.internship.hw_block_05_01.data.MailData;
import dev.profitsoft.internship.hw_block_05_01.enumeration.DeliveryStatus;
import dev.profitsoft.internship.hw_block_05_01.messaging.MailSentMessage;
import dev.profitsoft.internship.hw_block_05_01.repository.MailRepository;
import dev.profitsoft.internship.hw_block_05_01.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender emailSender;
    private final MailRepository mailRepository;
    private final String fromEmail;

    public MailServiceImpl(JavaMailSender emailSender, MailRepository mailRepository, @Value("${spring.mail.username}") String fromEmail) {
        this.emailSender = emailSender;
        this.mailRepository = mailRepository;
        this.fromEmail = fromEmail;
    }

    @Override
    public void handleMessage(MailSentMessage message) {
        SimpleMailMessage email = getMailMessage(message);
        MailData mailData = mapToMailDate(message);
        sendEmail(mailData, email);
    }

    @Override
    public void retryErrorMessageDelivery() {
        log.debug("Retrying error message delivery");
        mailRepository.findAllByStatus(DeliveryStatus.REJECTED.toString())
                .forEach(mailData -> {
                    SimpleMailMessage email = getMailMessage(mailData);
                    mailData.setAttemptDate(Instant.now());
                    mailData.setAttemptNumber(mailData.getAttemptNumber() + 1L);

                    sendEmail(mailData, email);
                });
    }

    private void sendEmail(MailData mailData, SimpleMailMessage email) {
        try {
            emailSender.send(email);
            mailData.setStatus(DeliveryStatus.DISPATCHED.toString());
            mailRepository.save(mailData);
        } catch (MailException e) {
            String errorMessage = e.getClass().getName() + " " + e.getMessage();
            mailData.setErrorMessage(errorMessage);
            mailData.setStatus(DeliveryStatus.REJECTED.toString());
            mailRepository.save(mailData);
        }
    }

    private SimpleMailMessage getMailMessage(MailData mailData) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(fromEmail);
        email.setTo(mailData.getEmail());
        email.setSubject(mailData.getSubject());
        email.setText(mailData.getContent());
        return email;
    }

    private SimpleMailMessage getMailMessage(MailSentMessage message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(fromEmail);
        email.setTo(message.getEmail());
        email.setSubject(message.getSubject());
        email.setText(message.getContent());
        return email;
    }

    private MailData mapToMailDate(MailSentMessage message) {
        return MailData.builder()
                .subject(message.getSubject())
                .content(message.getContent())
                .email(message.getEmail())
                .attemptDate(Instant.now())
                .attemptNumber(1L)
                .build();
    }
}
