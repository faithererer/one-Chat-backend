package com.zjc.onechat.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue messageQueue() {
        return new Queue("messageQueue", true); // 创建一个持久化队列
    }
}
