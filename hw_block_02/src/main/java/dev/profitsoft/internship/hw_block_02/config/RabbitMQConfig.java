package dev.profitsoft.internship.hw_block_02.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String MAIL_SENT_EXCHANGE = "mail-sent-exchange";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public FanoutExchange mailSentExchange() {
        return new FanoutExchange(MAIL_SENT_EXCHANGE, true, false);
    }
}
