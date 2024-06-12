package dev.profitsoft.internship.hw_block_05_01.messaging;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class MailSentMessage {
    private String subject;
    private String content;
    private String email;
}
