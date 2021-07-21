package hr.msimunovic.moneyheist.service;

import hr.msimunovic.moneyheist.common.enums.MemberSexEnum;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberInfoDTO;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.member.service.MemberService;
import hr.msimunovic.moneyheist.member.service.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

/*    Member member_1 = new Member(1L, "Tokyo", MemberSexEnum.F, "tokyo@ag04.com", MemberStatusEnum.AVAILABLE, new HashSet<>(), new HashSet<>());

    List<Member> members = new ArrayList<>();*/

    @Test
    void shouldReturnAllMembers() {

        /*when(memberRepository.findAll()).thenReturn(members);

        List<MemberInfoDTO> expected = memberService.getAllMembers();

        assertEquals(expected, members);*/

    }

}
