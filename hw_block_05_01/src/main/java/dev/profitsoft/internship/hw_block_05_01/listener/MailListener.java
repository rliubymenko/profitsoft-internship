package dev.profitsoft.internship.hw_block_05_01.listener;

import dev.profitsoft.internship.hw_block_05_01.messaging.MailSentMessage;
import dev.profitsoft.internship.hw_block_05_01.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static dev.profitsoft.internship.hw_block_05_01.config.RabbitMQConfig.MAIL_SENT_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailListener {

    private final MailService mailService;

    @RabbitListener(queues = MAIL_SENT_QUEUE)
    public void onMailSent(MailSentMessage message) {
        log.debug("Consumed message {},", message.toString());
        mailService.handleMessage(message);
    }
}
