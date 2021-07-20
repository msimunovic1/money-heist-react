package hr.msimunovic.moneyheist.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.MethodNotAllowedException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistInfoDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillsDTO;
import hr.msimunovic.moneyheist.heist.service.HeistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HeistController.class)
class HeistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private HeistService heistService;

    List<HeistInfoDTO> heists = new ArrayList<>(Arrays.asList(
            HeistInfoDTO.builder()
                    .id(1L)
                    .name("European Central Bank")
                    .status(HeistStatusEnum.READY)
                    .build(),
            HeistInfoDTO.builder()
                    .id(2L)
                    .name("Fábrica Nacional de Moneda y Timbre")
                    .status(HeistStatusEnum.FINISHED)
                    .build()));

    List<HeistSkillDTO> skills = new ArrayList<>(Arrays.asList(
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
    ));

    HeistDTO heistDTO = HeistDTO.builder()
            .name("European Central Bank")
            .location("Frankfurt, Germany")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusDays(5))
            .skills(skills)
            .status(HeistStatusEnum.READY)
            .build();

    HeistSkillsDTO heistSkillsDTO = HeistSkillsDTO.builder()
            .skills(skills)
            .build();

    @Test
    void getAllHeists_thenReturns200() throws Exception {

        when(heistService.getAllHeists()).thenReturn(heists);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/list")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void saveHeist_thenReturns201() throws Exception {

        when(heistService.saveHeist(heistDTO)).thenReturn(new Heist());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/heist")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void saveHeist_thenReturns400() throws Exception {

        when(heistService.saveHeist(heistDTO)).thenThrow(BadRequestException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/heist")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateSkills_thenReturns204() throws Exception {

        System.out.println(heistSkillsDTO);

        doNothing().when(heistService).updateSkills(1L, heistSkillsDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/heist/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistSkillsDTO)))
                .andExpect(status().isNoContent());

    }

    @Test
    void updateSkills_thenReturns400() throws Exception {

        doThrow(BadRequestException.class).when(heistService).updateSkills(1L, heistSkillsDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/heist/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistSkillsDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSkills_thenReturns404() throws Exception {

        doThrow(NotFoundException.class).when(heistService).updateSkills(1L, heistSkillsDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/heist/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistSkillsDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSkills_thenReturns405() throws Exception {

        doThrow(MethodNotAllowedException.class).when(heistService).updateSkills(1L, heistSkillsDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/heist/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistSkillsDTO)))
                .andExpect(status().isMethodNotAllowed());
    }

}
