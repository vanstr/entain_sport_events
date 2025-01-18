package com.entain.sport.events.service;

import com.entain.sport.events.dto.SportEventDto;
import com.entain.sport.events.model.EventStatus;
import com.entain.sport.events.model.SportType;
import org.springframework.stereotype.Service;

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
        events.add(dto);
        return dto;
    }

    public SportEventDto getById(Long id) {
        // TODO provide correct status code
        return events.stream()
                .filter(event -> Objects.equals(event.getId(), id))
                .findFirst().orElse(null);
    }

    public SportEventDto changeStatus(Long id, EventStatus status) {
        SportEventDto sportEventDto = getById(id);
        sportEventDto.setStatus(status);
        // TODO handle validation
        return sportEventDto;
    }
}
