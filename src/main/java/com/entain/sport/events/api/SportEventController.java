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

    @GetMapping("/{id}")
    public SportEventDto getSportEventById(@PathVariable Long id) {
        return events.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PatchMapping("/{id}/status")
    public SportEventDto changeSportEventStatus(@PathVariable Long id, @RequestParam String status) {
        SportEventDto event = events.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (event != null) {
            event.setStatus(status);
        }
        return event;
    }
}
