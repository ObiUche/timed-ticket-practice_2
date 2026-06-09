package com.independence.player.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrackingEventRequest(

        @NotNull
        @Size(min = 6)
        String athleteId,

        @NotNull
        @Size(min = 6)
        String sessionId,

        @NotNull
        double x,

        @NotNull
        double y
) {}
