package dev.profitsoft.internship.hw_block_05_01.config;

import jakarta.mail.internet.MimeMessage;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class JavaMailSenderTestConfiguration {

    private JavaMailSender javaMailSender;
    private MimeMessage mimeMessage;

    @Bean
    JavaMailSender mockMailSender() {
        mimeMessage = Mockito.mock(MimeMessage.class);
        javaMailSender = Mockito.mock(JavaMailSender.class);
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        return javaMailSender;
    }
}
