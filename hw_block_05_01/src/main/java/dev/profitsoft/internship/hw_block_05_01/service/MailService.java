package dev.profitsoft.internship.hw_block_05_01.service;

import dev.profitsoft.internship.hw_block_05_01.messaging.MailSentMessage;

public interface MailService {
    void handleMessage(MailSentMessage message);

    void retryErrorMessageDelivery();
}
