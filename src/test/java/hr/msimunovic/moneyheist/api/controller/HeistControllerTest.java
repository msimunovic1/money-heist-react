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
import hr.msimunovic.moneyheist.heist_member.dto.MembersEligibleForHeistDTO;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HeistController.class)
class HeistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private HeistService heistService;

    @Test
    void getAllHeists_thenReturns200() throws Exception {

        when(heistService.getAllHeists()).thenReturn(createHeists());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/list")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(createHeists())));

    }

    @Test
    void saveHeist_thenReturns201() throws Exception {

        when(heistService.saveHeist(createHeistDTO())).thenReturn(new Heist());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/heist")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistDTO())))
                .andExpect(status().isCreated());
    }

    @Test
    void saveHeist_thenReturns400() throws Exception {

        when(heistService.saveHeist(createHeistDTO())).thenThrow(BadRequestException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/heist")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistDTO())))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateSkills_thenReturns204() throws Exception {

        doNothing().when(heistService).updateSkills(1L, createHeistSkillsDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/heist/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistSkillsDTO())))
                .andExpect(status().isNoContent());

    }

    @Test
    void updateSkills_thenReturns400() throws Exception {

        doThrow(BadRequestException.class).when(heistService).updateSkills(1L, createHeistSkillsDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/heist/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistSkillsDTO())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSkills_thenReturns404() throws Exception {

        doThrow(NotFoundException.class).when(heistService).updateSkills(1L, createHeistSkillsDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/heist/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistSkillsDTO())))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSkills_thenReturns405() throws Exception {

        doThrow(MethodNotAllowedException.class).when(heistService).updateSkills(1L, createHeistSkillsDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/heist/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistSkillsDTO())))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void getMembersEligibleForHeist_thenReturns200() throws Exception {

        when(heistService.getMembersEligibleForHeist(1L)).thenReturn(createMembersEligibleForHeistDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/eligible_members")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

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

        doNothing().when(heistService).saveHeistMembers(1L, createHeistMembersDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistMembersDTO())))
                .andExpect(status().isNoContent());

    }

    @Test
    void saveHeistMembers_thenReturns400() throws Exception {

        doThrow(BadRequestException.class).when(heistService).saveHeistMembers(1L, createHeistMembersDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistMembersDTO())))
                .andExpect(status().isBadRequest());

    }

    @Test
    void saveHeistMembers_thenReturns404() throws Exception {

        doThrow(NotFoundException.class).when(heistService).saveHeistMembers(1L, createHeistMembersDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistMembersDTO())))
                .andExpect(status().isNotFound());

    }

    @Test
    void saveHeistMembers_thenReturns405() throws Exception {

        doThrow(MethodNotAllowedException.class).when(heistService).saveHeistMembers(1L, createHeistMembersDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/heist/1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(createHeistMembersDTO())))
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

        when(heistService.getHeistById(1L)).thenReturn(createHeistDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(createHeistDTO())));
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

        when(heistService.getHeistSkills(1L)).thenReturn(createSkills());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/skills")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(createSkills())));
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

        when(heistService.getHeistStatus(1L)).thenReturn(createHeistStatusDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/status")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(createHeistStatusDTO())));
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

        when(heistService.getHeistOutcome(1L)).thenReturn(createHeistOutcomeDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/outcome")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(createHeistOutcomeDTO())));
    }

    @Test
    void getHeistOutcome_thenReturns404() throws Exception {

        when(heistService.getHeistOutcome(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heist/1/outcome")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    private List<HeistInfoDTO> createHeists() {
        return Arrays.asList(
                HeistInfoDTO.builder()
                        .id(1L)
                        .name("European Central Bank")
                        .status(HeistStatusEnum.READY)
                        .build(),
                HeistInfoDTO.builder()
                        .id(2L)
                        .name("Fabrica Nacional de Moneda y Timbre")
                        .status(HeistStatusEnum.FINISHED)
                        .build());
    }

    private List<HeistSkillDTO> createSkills() {
        return Arrays.asList(
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
        );
    }

    private HeistDTO createHeistDTO() {
        return HeistDTO.builder()
                .name("European Central Bank")
                .location("Frankfurt, Germany")
                .skills(createSkills())
                .status(HeistStatusEnum.READY)
                .build();
    }

    private HeistSkillDTO createHeistSkillDTO() {
        return HeistSkillDTO.builder()
                .name("combat")
                .level("*")
                .members(2)
                .build();
    }

    private HeistSkillsDTO createHeistSkillsDTO() {
        return HeistSkillsDTO.builder()
                .skills(Arrays.asList(createHeistSkillDTO()))
                .build();
    }

    private List<SkillDTO> createMemberSkills() {
        return Arrays.asList(
                SkillDTO.builder()
                        .name("driving")
                        .level("****")
                        .build(),
                SkillDTO.builder()
                        .name("combat")
                        .level("*")
                        .build());
    }

    private HeistMemberDTO createHeistMemberDTO() {
        return HeistMemberDTO.builder()
                .id(1L)
                .name("Tokyo")
                .skills(createMemberSkills())
                .build();
    }

    List<HeistMemberDTO> heistMemberDTOList = Collections.singletonList(createHeistMemberDTO());

    private MembersEligibleForHeistDTO createMembersEligibleForHeistDTO() {
        return MembersEligibleForHeistDTO.builder()
                .skills(createSkills())
                .members(new HashSet<>(Collections.singletonList(createHeistMemberDTO())))
                .build();
    }

    private HeistMembersDTO createHeistMembersDTO() {
        return HeistMembersDTO.builder()
                .members(new String[]{
                        "Tokyo",
                        "Denver",
                        "Berlin"
                })
                .build();
    }

    private HeistStatusDTO createHeistStatusDTO() {
        return HeistStatusDTO.builder()
                .status(HeistStatusEnum.READY)
                .build();
    }

    private HeistOutcomeDTO createHeistOutcomeDTO() {
        return HeistOutcomeDTO.builder()
                .outcome(HeistOutcomeEnum.SUCCEEDED)
                .build();
    }

}
