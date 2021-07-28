package hr.msimunovic.moneyheist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.msimunovic.moneyheist.common.enums.MemberSexEnum;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.member.dto.MemberInfoDTO;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;
import hr.msimunovic.moneyheist.member.mapper.MemberMapper;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.member.service.MemberServiceImpl;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
import hr.msimunovic.moneyheist.member_skill.MemberSkillId;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private SkillMapper skillMapper;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<Member> memberArgumentCaptor;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member member;

    private Skill skill;

    @BeforeEach
    void setup() {

        skill = new Skill();
        skill.setId(1L);
        skill.setName("combat");
        skill.setLevel("*");

        member = new Member();
        member.setId(1L);
        member.setName("Tokyo");
        member.setSex(MemberSexEnum.F);
        member.setStatus(MemberStatusEnum.AVAILABLE);

    }

    @Test
    void shouldReturnAllMembers() {

        when(memberRepository.findAll()).thenReturn(Arrays.asList(member));

        when(modelMapper.map(member, MemberInfoDTO.class)).thenReturn(createMemberInfoDTO());

        List<MemberInfoDTO> expected = memberService.getAllMembers();

        assertThat(expected).isEqualTo(Arrays.asList(createMemberInfoDTO()));

    }

    @Test
    void shouldSaveMember() {

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(null);

        when(memberMapper.mapDTOToMember(createMemberDTO())).thenReturn(member);

        when(memberRepository.save(member)).thenReturn(member);

        Long expected = memberService.saveMember(createMemberDTO());

        assertThat(expected).isEqualTo(member.getId());

    }

    @Test
    void shouldDeleteSkill() {

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        when(skillRepository.findByName("combat")).thenReturn(Arrays.asList(skill));

        when(memberRepository.save(member)).thenReturn(member);

        memberService.deleteSkill(1L, "combat");

        verify(memberRepository).save(memberArgumentCaptor.capture());

        assertThat(memberArgumentCaptor.getValue().getId()).isEqualTo(1L);

    }

    @Test
    void shouldReturnMemberById() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        log.info(mapper.writeValueAsString(member));

        when(memberRepository.findById(100L)).thenReturn(Optional.of(member));

        when(memberMapper.mapMemberToDTO(member)).thenReturn(createMemberDTO());

        MemberDTO expected = memberService.getMemberById(100L);

        assertEquals(expected, createMemberDTO());

        assertThat(expected).isEqualTo(createMemberDTO());

    }

    private MemberInfoDTO createMemberInfoDTO() {
        return MemberInfoDTO.builder()
                .id(1L)
                .name("Tokyo")
                .status(MemberStatusEnum.AVAILABLE)
                .build();
    }

    private List<SkillDTO> createSkillDTOList() {
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

    private MemberDTO createMemberDTO() {
        return MemberDTO.builder()
                .name("Tokyo")
                .sex(MemberSexEnum.F)
                .skills(createSkillDTOList())
                .mainSkill("combat")
                .status(MemberStatusEnum.AVAILABLE)
                .build();
    }

    private MemberSkillDTO createMemberSkillDTO() {
        return MemberSkillDTO.builder()
                .skills(createSkillDTOList())
                .mainSkill("combat")
                .build();
    }

}
