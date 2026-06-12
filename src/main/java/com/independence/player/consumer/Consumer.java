package com.independence.player.consumer;


import com.independence.player.event.AthletePositionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @KafkaListener(topics = "athlete_position_event")
    public void printEvent(AthletePositionEvent event){
       System.out.println("AthleteId: " + event.athleteId());
       System.out.println("SessionId: " + event.sessionId());
       System.out.println("X: " + event.x());
       System.out.println("Y: " + event.y());
       System.out.println("Time: " + event.timestamp());
       System.out.println("Source: " + event.source());
    }

}