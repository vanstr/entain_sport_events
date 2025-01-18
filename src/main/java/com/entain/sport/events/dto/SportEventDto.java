package com.entain.sport.events.dto;

import com.entain.sport.events.model.EventStatus;
import com.entain.sport.events.model.SportType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SportEventDto {
    private Long id;
    private String name;
    private SportType sport;
    private EventStatus status;
    private LocalDateTime startTime;
}