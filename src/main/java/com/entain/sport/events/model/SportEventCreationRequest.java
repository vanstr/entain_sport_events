package com.entain.sport.events.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
public class SportEventCreationRequest {
    private String name;
    @NonNull
    private SportType sport;
    @NonNull
    private ZonedDateTime startTime;
}