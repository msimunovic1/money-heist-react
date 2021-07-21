package hr.msimunovic.moneyheist.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.enums.MemberSexEnum;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.member.dto.MemberInfoDTO;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;
import hr.msimunovic.moneyheist.member.service.MemberService;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MemberService memberService;

    List<MemberInfoDTO> members = new ArrayList<>(Arrays.asList(
            MemberInfoDTO.builder()
                    .id(1L)
                    .name("Tokyo")
                    .status(MemberStatusEnum.AVAILABLE)
                    .build(),
            MemberInfoDTO.builder()
                    .id(2L)
                    .name("Denver")
                    .status(MemberStatusEnum.RETIRED)
                    .build()));

    List<SkillDTO> skills = new ArrayList<>(Arrays.asList(
            SkillDTO.builder()
                    .name("driving")
                    .level("****")
                    .build(),
            SkillDTO.builder()
                    .name("combat")
                    .level("*")
                    .build()));

    MemberDTO memberDTO = MemberDTO.builder()
            .name("Nairobi")
            .sex(MemberSexEnum.F)
            .email("nairobi@ag04.com")
            .skills(skills)
            .mainSkill("combat")
            .status(MemberStatusEnum.AVAILABLE)
            .build();

    MemberSkillDTO memberSkillDTO = MemberSkillDTO.builder()
            .skills(skills)
            .mainSkill("combat")
            .build();

    @Test
    void getAllMembers_thenReturns200() throws Exception {

        when(memberService.getAllMembers()).thenReturn(members);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/member/list")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().string(this.mapper.writeValueAsString(members)));

    }

    @Test
    void saveMember_thenReturns201() throws Exception {

        when(memberService.saveMember(memberDTO)).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(memberDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void saveMember_thenReturns400() throws Exception {

        when(memberService.saveMember(memberDTO)).thenThrow(BadRequestException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(memberDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateSkills_thenReturns204() throws Exception {

        doNothing().when(memberService).updateSkills(1L, memberSkillDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/member/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(memberSkillDTO)))
                .andExpect(status().isNoContent());

    }

    @Test
    void updateSkills_thenReturns400() throws Exception {

        doThrow(BadRequestException.class).when(memberService).updateSkills(1L, memberSkillDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/member/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(memberSkillDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateSkills_thenReturns404() throws Exception {

        doThrow(NotFoundException.class).when(memberService).updateSkills(1L, memberSkillDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/member/1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(memberSkillDTO)))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteSkill_thenReturns204() throws Exception {

        doNothing().when(memberService).deleteSkill(1L, "combat");

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/member/1/skills/combat")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteSkill_thenReturns404() throws Exception {

        doThrow(NotFoundException.class).when(memberService).deleteSkill(1L, "combat");

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/member/1/skills/combat")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void getMemberById_thenReturns200() throws Exception {

        when(memberService.getMemberById(1L)).thenReturn(memberDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/member/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(memberDTO)));
    }

    @Test
    void getMemberById_thenReturns404() throws Exception {

        when(memberService.getMemberById(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/member/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSkillsByMemberId_thenReturns200() throws Exception {

        when(memberService.getMemberSkills(1L)).thenReturn(memberSkillDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/member/1/skills")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(this.mapper.writeValueAsString(memberSkillDTO)));
    }

    @Test
    void getSkillsByMemberId_thenReturns404() throws Exception {

        when(memberService.getMemberSkills(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/member/1/skills")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
