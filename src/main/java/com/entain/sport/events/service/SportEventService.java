package com.entain.sport.events.service;

import com.entain.sport.events.SportEventNotFoundException;
import com.entain.sport.events.dto.SportEventDto;
import com.entain.sport.events.model.EventStatus;
import com.entain.sport.events.model.SportType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SportEventService {

    private final List<SportEventDto> events = new CopyOnWriteArrayList<>();
    private final AtomicLong id = new AtomicLong(0);

    public List<SportEventDto> getSportEvents(EventStatus status, SportType sport) {
        return events.stream().filter(event -> {
            if (status != null && !Objects.equals(event.getStatus(), status)) {
                return false;
            }
            if (sport != null && !Objects.equals(event.getSport(), sport)) {
                return false;
            }
            return true;
        }).toList();
    }

    public SportEventDto createSportEvent(SportEventDto dto) {
        dto.setId(id.incrementAndGet());
        dto.setStatus(EventStatus.INACTIVE);
        events.add(dto);
        return dto;
    }

    public SportEventDto getById(Long id) {
        return events.stream()
                .filter(event -> Objects.equals(event.getId(), id))
                .findFirst().orElseThrow(() -> new SportEventNotFoundException("Sport event not found: " + id));
    }

    public SportEventDto changeStatus(Long id, EventStatus newStatus) {
        SportEventDto sportEvent = getById(id);
        EventStatus currentStatus = sportEvent.getStatus();

        if (currentStatus == EventStatus.INACTIVE && newStatus == EventStatus.ACTIVE) {
            if (sportEvent.getStartTime().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Cannot activate event before the start time");
            }
            sportEvent.setStatus(EventStatus.ACTIVE);
        } else if (currentStatus == EventStatus.ACTIVE && newStatus == EventStatus.FINISHED) {
            sportEvent.setStatus(EventStatus.FINISHED);
        } else {
            throw new IllegalArgumentException("Cannot change status from " + currentStatus + " to " + newStatus);
        }

        return sportEvent;
    }
}
