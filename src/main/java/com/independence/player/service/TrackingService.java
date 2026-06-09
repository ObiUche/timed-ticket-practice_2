package com.independence.player.service;

import com.independence.player.dto.TrackingEventRequest;
import com.independence.player.event.AthletePositionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {

    private final KafkaTemplate<String, AthletePositionEvent> kafkaTemplate;

    public TrackingService(KafkaTemplate<String,AthletePositionEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public String publishEvent(TrackingEventRequest request){

        // Map to the event shape
        AthletePositionEvent event = AthletePositionEvent.from(request);

        // Now i need to pass to producer
        kafkaTemplate.sendDefault(event.athleteId(), event);

        return "Player tracking sent to Kafka";


    }
}
