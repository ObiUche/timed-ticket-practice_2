package com.independence.player.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String ATHLETE_POSITION_EVENT = "athlete_position_event";

    @Bean
    public NewTopic trackingEventTopic(){
        // Create a new topic
        return new NewTopic(ATHLETE_POSITION_EVENT,
                1,
                (short) 1);
    }

}