package com.entain.sport.events.api;

import com.entain.sport.events.dto.SportEventDto;
import com.entain.sport.events.model.EventStatus;
import com.entain.sport.events.model.SportType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SportEventControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSportEventHappyPath() throws Exception {
        SportEventDto dto = new SportEventDto();
        dto.setName("Football Match");
        dto.setSport(SportType.FOOTBALL);
        dto.setStatus(EventStatus.INACTIVE);
        dto.setStartTime(LocalDateTime.now().plusDays(1));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/sport-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Football Match"))
                .andExpect(jsonPath("$.sport").value("FOOTBALL"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.startTime").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SportEventDto.class).getId();

        mockMvc.perform(get("/api/v1/sport-events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Football Match"));

        mockMvc.perform(get("/api/v1/sport-events/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Football Match"));

        updateStatus(id, "ACTIVE");

        updateStatus(id, "FINISHED");
    }

    @Test
    void testGetSportEventsByStatus() throws Exception {
        createSportEvent(SportType.FOOTBALL);
        createSportEvent(SportType.TENNIS);
        createSportEvent(SportType.TENNIS);

        mockMvc.perform(get("/api/v1/sport-events")
                        .param("sport", "TENNIS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void testGetSportEventsBySportType() throws Exception {
        Long sportEvent1 = createSportEvent(SportType.FOOTBALL);
        updateStatus(sportEvent1, "ACTIVE");
        Long sportEvent2 = createSportEvent(SportType.TENNIS);
        updateStatus(sportEvent2, "ACTIVE");

        createSportEvent(SportType.TENNIS);

        mockMvc.perform(get("/api/v1/sport-events")
                        .param("status", "ACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    private void updateStatus(Long sportEventId, String newStatus) throws Exception {
        mockMvc.perform(patch("/api/v1/sport-events/" + sportEventId + "/status")
                        .param("status", newStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(newStatus));
    }

    private Long createSportEvent(SportType sportType) throws Exception {
        SportEventDto dto = new SportEventDto();
        dto.setName(sportType + " Match");
        dto.setSport(sportType);
        dto.setStatus(EventStatus.INACTIVE);
        dto.setStartTime(LocalDateTime.now().plusDays(1));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/sport-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SportEventDto.class).getId();

    }

    private String asJsonString(SportEventDto dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

}