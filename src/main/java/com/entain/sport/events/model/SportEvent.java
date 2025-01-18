package com.entain.sport.events.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
public class SportEvent {
    private Long id;
    private String name;
    @NonNull
    private SportType sport;
    private EventStatus status;
    @NonNull
    private ZonedDateTime startTime;
}