package hr.msimunovic.moneyheist.api.controller;

import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import hr.msimunovic.moneyheist.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class HeistControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllHeists_thenReturns200() throws Exception {

        mockMvc.perform(get("/heist/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

    }

    @Test
    void saveHeist_thenReturns201() throws Exception {

        mockMvc.perform(post("/heist")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(createHeistDTO())))
                .andExpect(status().isCreated());
    }

    private HeistDTO createHeistDTO() {
        return HeistDTO.builder()
                .name("European Central Bank")
                .location("Frankfurt, Germany")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(5))
                .skills(Arrays.asList(
                        HeistSkillDTO.builder()
                                .name("combat")
                                .level("***")
                                .members(2)
                                .build(),
                        HeistSkillDTO.builder()
                                .name("driving")
                                .level("*")
                                .members(1)
                                .build()
                ))
                .build();
    }
}
