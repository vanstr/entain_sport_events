package com.entain.sport.events.api;

import com.entain.sport.events.model.SportEventCreationRequest;
import com.entain.sport.events.model.SportEvent;
import com.entain.sport.events.model.EventStatus;
import com.entain.sport.events.model.SportType;
import com.entain.sport.events.service.SportEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sport-events")
public class SportEventController {

    private final SportEventService service;

    @GetMapping
    public List<SportEvent> getSportEvents(@RequestParam(required = false) EventStatus status,
                                           @RequestParam(required = false) SportType sport) {
        return service.getSportEvents(status, sport);
    }

    @PostMapping
    public SportEvent createSportEvent(@RequestBody SportEventCreationRequest creationRequest) {
       return service.createSportEvent(creationRequest);
    }

    @GetMapping("/{id}")
    public SportEvent getSportEventById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}/status")
    public SportEvent changeSportEventStatus(@PathVariable Long id, @RequestParam EventStatus status) {
        return service.changeStatus(id, status);
    }
}
