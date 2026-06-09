package com.independence.player.consumer;

import com.independence.player.event.AthletePositionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @KafkaListener(topics = "athlete-position-events")
    public void printEvent(AthletePositionEvent event){
       System.out.printf("AthleteId: %s", event.athleteId());
       System.out.printf("SessionId: %s", event.sessionId());
       System.out.printf("X: %f", event.x());
       System.out.printf("Y: %f", event.y());
       System.out.printf("Timestamp: %tT", event.timestamp());
       System.out.printf("Timestamp: %s", event.source());
    }
}
