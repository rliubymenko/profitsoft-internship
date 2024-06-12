package dev.profitsoft.internship.hw_block_05_01.schedule;

import dev.profitsoft.internship.hw_block_05_01.service.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@AllArgsConstructor
public class NotificationRepeater {

    private final MailService mailService;

    @Scheduled(fixedDelay = 300_000)
    public void scheduleErrorMessageCheck() {
        log.debug("Scheduling error message");
        mailService.retryErrorMessageDelivery();
    }
}
