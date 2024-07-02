package dev.profitsoft.internship.hw_block_05_01.service.impl;

import dev.profitsoft.internship.hw_block_05_01.config.ElasticsearchTestConfiguration;
import dev.profitsoft.internship.hw_block_05_01.data.MailData;
import dev.profitsoft.internship.hw_block_05_01.enumeration.DeliveryStatus;
import dev.profitsoft.internship.hw_block_05_01.messaging.MailSentMessage;
import dev.profitsoft.internship.hw_block_05_01.service.MailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ElasticsearchTestConfiguration.class})
class MailServiceImplTest {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private MailService mailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @BeforeEach
    public void beforeEach() {
        elasticsearchOperations.indexOps(MailData.class).createMapping();
    }

    @AfterEach
    public void afterEach() {
        elasticsearchOperations.indexOps(MailData.class).delete();
    }

    @Test
    void shouldSendEmailAndSaveMessage() {
        Mockito.doNothing().when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));

        MailSentMessage message = MailSentMessage.builder()
                .email("email@example.com")
                .subject("Subject")
                .content("Content")
                .build();

        mailService.handleMessage(message);

        SearchHits<MailData> actualData = elasticsearchOperations.search(Query.findAll(), MailData.class);
        SearchHit<MailData> mailData = actualData.iterator().next();
        assertThat(mailData.getContent().getContent()).isEqualTo(message.getContent());
        assertThat(mailData.getContent().getSubject()).isEqualTo(message.getSubject());
        assertThat(mailData.getContent().getEmail()).isEqualTo(message.getEmail());
        assertThat(mailData.getContent().getStatus()).isEqualTo(DeliveryStatus.DISPATCHED.toString());
        assertThat(mailData.getContent().getAttemptNumber()).isEqualTo(1L);
    }

    @Test
    void shouldFailSendingMessageAndSaveError() {
        Mockito.doThrow(new MailAuthenticationException("Authentication Error")).when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));

        MailSentMessage message = MailSentMessage.builder()
                .email("email@example.com")
                .subject("Subject")
                .content("Content")
                .build();

        mailService.handleMessage(message);

        SearchHits<MailData> actualData = elasticsearchOperations.search(Query.findAll(), MailData.class);
        SearchHit<MailData> mailData = actualData.iterator().next();
        assertThat(mailData.getContent().getContent()).isEqualTo(message.getContent());
        assertThat(mailData.getContent().getSubject()).isEqualTo(message.getSubject());
        assertThat(mailData.getContent().getEmail()).isEqualTo(message.getEmail());
        assertThat(mailData.getContent().getStatus()).isEqualTo(DeliveryStatus.REJECTED.toString());
        assertThat(mailData.getContent().getAttemptNumber()).isEqualTo(1L);
    }
}