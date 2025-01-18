package com.entain.sport.events.api;

import com.entain.sport.events.dto.SportEventDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sport-events")
public class SportEventController {

    private List<SportEventDto> events = new ArrayList<>();

    @GetMapping
    public List<SportEventDto> getSportEvents() {
        return events;
    }

    @PostMapping
    public SportEventDto createSportEvent(@RequestBody SportEventDto dto) {
        events.add(dto);
        dto.setId((long) events.size());
        return dto;
    }
}
