package com.entain.sport.events.api;

import com.entain.sport.events.model.SportEvent;
import com.entain.sport.events.model.SportEventCreationRequest;
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

import java.time.ZonedDateTime;

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
        SportEventCreationRequest request = new SportEventCreationRequest();
        request.setName("Football Match");
        request.setSport(SportType.FOOTBALL);
        request.setStartTime(ZonedDateTime.now().plusDays(1));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/sport-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Football Match"))
                .andExpect(jsonPath("$.sport").value("FOOTBALL"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.startTime").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SportEvent.class).getId();

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

    @Test
    void testStatusCanBeChangedFromInactiveToActiveAndAfterToFinished() throws Exception {
        Long sportEventId = createSportEvent(SportType.FOOTBALL);
        updateStatus(sportEventId, "ACTIVE");
        updateStatus(sportEventId, "FINISHED");
    }

    @Test
    void testCanNotActivatePastEvents() throws Exception {
        Long sportEventId = createSportEventInThePast();

        mockMvc.perform(patch("/api/v1/sport-events/" + sportEventId + "/status")
                        .param("status", "ACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testCanChangeStatusForFinishedEvents() throws Exception {
        Long sportEventId = createSportEvent(SportType.FOOTBALL);
        updateStatus(sportEventId, "ACTIVE");
        updateStatus(sportEventId, "FINISHED");

        mockMvc.perform(patch("/api/v1/sport-events/" + sportEventId + "/status")
                        .param("status", "ACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch("/api/v1/sport-events/" + sportEventId + "/status")
                        .param("status", "INACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCanNotChangeStatusFromInactiveToFinished() throws Exception {
        Long sportEventId = createSportEvent(SportType.FOOTBALL);

        mockMvc.perform(patch("/api/v1/sport-events/" + sportEventId + "/status")
                        .param("status", "FINISHED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private void updateStatus(Long sportEventId, String newStatus) throws Exception {
        mockMvc.perform(patch("/api/v1/sport-events/" + sportEventId + "/status")
                        .param("status", newStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(newStatus));
    }

    private Long createSportEvent(SportType sportType) throws Exception {
        SportEventCreationRequest request = new SportEventCreationRequest();
        request.setName(sportType + " Match");
        request.setSport(sportType);
        request.setStartTime(ZonedDateTime.now().plusDays(1));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/sport-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SportEvent.class).getId();

    }

    private Long createSportEventInThePast() throws Exception {
        SportEventCreationRequest request = new SportEventCreationRequest();
        request.setName("Past Match");
        request.setSport(SportType.FOOTBALL);
        request.setStartTime(ZonedDateTime.now().minusDays(1));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/sport-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SportEvent.class).getId();
    }

    private String asJsonString(SportEventCreationRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }

}