package com.independence.player.controller;

import com.independence.player.dto.TrackingEventRequest;
import com.independence.player.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tracking-events")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService){
        this.trackingService = trackingService;
    }

    @PostMapping
    public ResponseEntity<String> receiveEvent(@Valid @RequestBody TrackingEventRequest request){
        // Delegate to the service
        String response = trackingService.publishEvent(request);

        return ResponseEntity.ok(response);

    }
}
