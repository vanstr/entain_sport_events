package com.entain.sport.events.dto;

import com.entain.sport.events.model.EventStatus;
import com.entain.sport.events.model.SportType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
public class SportEventDto {
    private Long id;
    private String name;
    @NonNull
    private SportType sport;
    private EventStatus status;
    @NonNull
    private ZonedDateTime startTime;
}