package com.entain.sport.events.api;

import com.entain.sport.events.dto.SportEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SportEventControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateSportEvent() throws Exception {
        SportEventDto dto = new SportEventDto();
        dto.setName("Football Match");
        dto.setSport("Football");
        dto.setStatus("INACTIVE");
        dto.setStartTime(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/api/v1/sport-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Football Match"))
                .andExpect(jsonPath("$.sport").value("Football"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.startTime").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty());

    }

    private String asJsonString(SportEventDto dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

}