package com.independence.player.service;

import com.independence.player.config.KafkaTopicConfig;
import com.independence.player.dto.TrackingEventRequest;
import com.independence.player.event.AthletePositionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {

    // in order to send a message i need to instantiate the kafkaTemplate
    private final KafkaTemplate<String, AthletePositionEvent> kafkaTemplate;

    public TrackingService(KafkaTemplate<String, AthletePositionEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }


    public String publishEvent(TrackingEventRequest request){
        // receive the request then transform the request
        AthletePositionEvent event = AthletePositionEvent.from(request);

        // now build the message
        kafkaTemplate.send(KafkaTopicConfig.ATHLETE_POSITION_EVENT,
                event.athleteId(),
                event);
       return "event processed";
    }
}