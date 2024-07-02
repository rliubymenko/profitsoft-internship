package dev.profitsoft.internship.hw_block_05_01.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RabbitMQConfig {

    public static final String MAIL_SENT_QUEUE = "mail-sent-queue";
    public static final String MAIL_SENT_EXCHANGE = "mail-sent-exchange";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue mailSentQueue() {
        return new Queue(MAIL_SENT_QUEUE, false);
    }

    @Bean
    public FanoutExchange mailSentExchange() {
        return new FanoutExchange(MAIL_SENT_EXCHANGE, true, false);
    }

    @Bean
    public Binding mailSentBinding() {
        return BindingBuilder.bind(mailSentQueue()).to(mailSentExchange());
    }
}
