package com.independence.player.event;

import com.independence.player.dto.TrackingEventRequest;

import java.time.Instant;

public record AthletePositionEvent(
        String athleteId,
        String sessionId,
        double x,
        double y,
        Instant timestamp,
        String source
) {
    public static AthletePositionEvent from(TrackingEventRequest request){
       return new AthletePositionEvent(
                request.athleteId(),
                request.sessionId(),
                request.x(),
                request.y(),
                Instant.now(),
                "REST_API"
        );
    }
}
