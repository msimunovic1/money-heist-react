package hr.msimunovic.moneyheist.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.MethodNotAllowedException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.enums.HeistOutcomeEnum;
import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.*;
import hr.msimunovic.moneyheist.heist.service.HeistService;
import hr.msimunovic.moneyheist.heistMember.dto.MembersEligibleForHeistDTO;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.*;

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
                    .name("Fabrica Nacional de Moneda y Timbre")
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

    List<SkillDTO> memberSkills = new ArrayList<>(Arrays.asList(
            SkillDTO.builder()
                    .name("driving")
                    .level("****")
                    .build(),
            SkillDTO.builder()
                    .name("combat")
                    .level("*")
                    .build()));

    HeistMemberDTO heistMemberDTO = HeistMemberDTO.builder()
            .id(1L)
            .name("Tokyo")
            .skills(memberSkills)
            .build();

    List<HeistMemberDTO> heistMemberDTOList = new ArrayList<>(Collections.singletonList(heistMemberDTO));

    MembersEligibleForHeistDTO membersEligibleForHeistDTO = MembersEligibleForHeistDTO.builder()
            .skills(skills)
            .members(new HashSet<>(Collections.singletonList(heistMemberDTO)))
            .build();

    HeistMembersDTO heistMembersDTO = HeistMembersDTO.builder()
            .members(new String[]{
                    "Tokyo",
                    "Denver",
                    "Berlin"
            })
            .build();

    HeistStatusDTO heistStatusDTO = HeistStatusDTO.builder()
            .status(HeistStatusEnum.READY)
            .build();

    HeistOutcomeDTO heistOutcomeDTO = HeistOutcomeDTO.builder()
            .outcome(HeistOutcomeEnum.SUCCEEDED)
            .build();

    @Test
    void getAllHeists_thenReturns200() throws Exception {

        when(heistService.getAllHeists()).thenReturn(heists);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/list")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(heists)));

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

    @Test
    void getMembersEligibleForHeist_thenReturns200() throws Exception {

        when(heistService.getMembersEligibleForHeist(1L)).thenReturn(membersEligibleForHeistDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/eligible_members")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(membersEligibleForHeistDTO)));

    }

    @Test
    void getMembersEligibleForHeist_thenReturns404() throws Exception {

        when(heistService.getMembersEligibleForHeist(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/eligible_members")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getMembersEligibleForHeist_thenReturns405() throws Exception {

        when(heistService.getMembersEligibleForHeist(1L)).thenThrow(MethodNotAllowedException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/eligible_members")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void saveHeistMembers_thenReturns204() throws Exception {

        doNothing().when(heistService).saveHeistMembers(1L, heistMembersDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistMembersDTO)))
                .andExpect(status().isNoContent());

    }

    @Test
    void saveHeistMembers_thenReturns400() throws Exception {

        doThrow(BadRequestException.class).when(heistService).saveHeistMembers(1L, heistMembersDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistMembersDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void saveHeistMembers_thenReturns404() throws Exception {

        doThrow(NotFoundException.class).when(heistService).saveHeistMembers(1L, heistMembersDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistMembersDTO)))
                .andExpect(status().isNotFound());

    }

    @Test
    void saveHeistMembers_thenReturns405() throws Exception {

        doThrow(MethodNotAllowedException.class).when(heistService).saveHeistMembers(1L, heistMembersDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(heistMembersDTO)))
                .andExpect(status().isMethodNotAllowed());

    }

    @Test
    void startHeistManually_thenReturns200() throws Exception {

        doNothing().when(heistService).startHeist(1L);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void startHeistManually_thenReturns404() throws Exception {

        doThrow(NotFoundException.class).when(heistService).startHeist(1L);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void startHeistManually_thenReturns405() throws Exception {

        doThrow(MethodNotAllowedException.class).when(heistService).startHeist(1L);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

    }

    @Test
    void getHeistById_thenReturns200() throws Exception {

        when(heistService.getHeistById(1L)).thenReturn(heistDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(heistDTO)));
    }

    @Test
    void getHeistById_thenReturns404() throws Exception {

        when(heistService.getHeistById(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHeistMembers_thenReturns200() throws Exception {

        when(heistService.getHeistMembers(1L)).thenReturn(heistMemberDTOList);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/members")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(heistMemberDTOList)));
    }

    @Test
    void getHeistMembers_thenReturns404() throws Exception {

        when(heistService.getHeistMembers(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/members")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void getHeistMembers_thenReturns405() throws Exception {

        when(heistService.getHeistMembers(1L)).thenThrow(MethodNotAllowedException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/members")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

    }

    @Test
    void getSkillsByHeistId_thenReturns200() throws Exception {

        when(heistService.getHeistSkills(1L)).thenReturn(skills);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/skills")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(skills)));
    }

    @Test
    void getSkillsByHeistId_thenReturns404() throws Exception {

        when(heistService.getHeistSkills(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/skills")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void getHeistStatus_thenReturns200() throws Exception {

        when(heistService.getHeistStatus(1L)).thenReturn(heistStatusDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/status")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(heistStatusDTO)));
    }

    @Test
    void getHeistStatus_thenReturns404() throws Exception {

        when(heistService.getHeistStatus(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/status")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void getHeistOutcome_thenReturns200() throws Exception {

        when(heistService.getHeistOutcome(1L)).thenReturn(heistOutcomeDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/outcome")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(heistOutcomeDTO)));
    }

    @Test
    void getHeistOutcome_thenReturns404() throws Exception {

        when(heistService.getHeistOutcome(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/outcome")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}
