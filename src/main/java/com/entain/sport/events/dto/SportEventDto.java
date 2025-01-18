package com.entain.sport.events.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SportEventDto {
    private Long id;
    private String name;
    private String sport;
    private String status;
    private LocalDateTime startTime;
}