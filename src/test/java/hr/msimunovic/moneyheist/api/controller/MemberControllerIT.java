package hr.msimunovic.moneyheist.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.member.dto.MemberInfoDTO;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() throws JsonProcessingException, JSONException {


        String response = this.restTemplate.getForObject("/member/list", String.class);
        JSONAssert.assertEquals(createMembers(), response, false);
    }

    private String createMembers() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Arrays.asList(
                MemberInfoDTO.builder()
                        .id(100L)
                        .name("Tokyo")
                        .status(MemberStatusEnum.AVAILABLE)
                        .build(),
                MemberInfoDTO.builder()
                        .id(101L)
                        .name("Berlin")
                        .status(MemberStatusEnum.AVAILABLE)
                        .build(),
                MemberInfoDTO.builder()
                        .id(102L)
                        .name("Moscow")
                        .status(MemberStatusEnum.INCARCERATED)
                        .build()));
    }
}
