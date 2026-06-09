package com.independence.player.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;

public class Topic {

    @Bean
    public NewTopic defaultTopic(){
        return new NewTopic("athlete-position-events",
                1,
                (short) 1);
    }
}
