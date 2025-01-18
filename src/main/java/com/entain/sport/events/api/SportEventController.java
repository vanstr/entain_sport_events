package com.entain.sport.events.api;

import com.entain.sport.events.dto.SportEventDto;
import com.entain.sport.events.service.SportEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sport-events")
public class SportEventController {

    private final SportEventService service;

    @GetMapping
    public List<SportEventDto> getSportEvents() {
        return service.getAll();
    }

    @PostMapping
    public SportEventDto createSportEvent(@RequestBody SportEventDto dto) {
       return service.createSportEvent(dto);
    }

    @GetMapping("/{id}")
    public SportEventDto getSportEventById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}/status")
    public SportEventDto changeSportEventStatus(@PathVariable Long id, @RequestParam String status) {
        return service.changeStatus(id, status);
    }
}
