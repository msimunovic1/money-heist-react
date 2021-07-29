package hr.msimunovic.moneyheist.api.controller;

import hr.msimunovic.moneyheist.common.enums.MemberSexEnum;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MemberControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllMembers_thenReturns200() throws Exception {

        mockMvc.perform(get("/member/list")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));

    }

    @Test
    void saveMember_thenReturns201() throws Exception {

        mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(createMemberDTO())))
                .andExpect(status().isCreated());
    }

    private MemberDTO createMemberDTO() {
        return MemberDTO.builder()
                .name("Nairobi")
                .sex(MemberSexEnum.F)
                .email("nairobi@ag04.com")
                .skills(Arrays.asList(
                        SkillDTO.builder()
                                .name("driving")
                                .level("****")
                                .build(),
                        SkillDTO.builder()
                                .name("combat")
                                .level("*")
                                .build()))
                .mainSkill("combat")
                .status(MemberStatusEnum.AVAILABLE)
                .build();
    }
}
