package hr.msimunovic.moneyheist.service;

import hr.msimunovic.moneyheist.common.enums.HeistOutcomeEnum;
import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import hr.msimunovic.moneyheist.common.enums.MemberSexEnum;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.*;
import hr.msimunovic.moneyheist.heist.mapper.HeistMapper;
import hr.msimunovic.moneyheist.heist.repository.HeistRepository;
import hr.msimunovic.moneyheist.heist.service.HeistServiceImpl;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.mapper.MemberMapper;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.member_skill.member_skill_improvement.MemberSkillImprovement;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
class HeistServiceTest {

    @Mock
    private HeistRepository heistRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private HeistMapper heistMapper;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private SkillMapper skillMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ThreadPoolTaskScheduler taskScheduler;

    @Mock
    private PeriodicTrigger periodicTrigger;

    @Mock
    private MemberSkillImprovement memberSkillImprovement;

    @InjectMocks
    private HeistServiceImpl heistService;

    @Captor
    private ArgumentCaptor<Heist> heistArgumentCaptor;

    Heist heist = new Heist(1L, "European Central Bank", "Frankfurt, Germany", null,
            null, HeistStatusEnum.READY, HeistOutcomeEnum.SUCCEEDED, null, null);

    Skill skill = new Skill(1L, "combat", "*", null, null);

    Member member = new Member(1L, "Tokyo", MemberSexEnum.F, "tokyo@ag04.com", MemberStatusEnum.AVAILABLE, null, null);

    @Test
    void shouldSaveHeist() {

        when(heistRepository.findByName(heist.getName())).thenReturn(null);

        when(heistMapper.mapDTOToHeist(createHeistDTO())).thenReturn(heist);

        when(heistRepository.save(heist)).thenReturn(heist);

        Heist expected = heistService.saveHeist(createHeistDTO());

        assertThat(expected).isEqualTo(heist);

    }

    @Test
    void shouldUpdateSkills() {


        when(heistRepository.findById(1L)).thenReturn(Optional.of(heist));

        when(skillRepository.findByNameIgnoreCaseAndLevel("combat", "*")).thenReturn(skill);

        heistService.updateSkills(1L, createHeistSkillsDTO());

        //verify(heistRepository).save(heistArgumentCaptor.capture());

    }

    @Test
    void shouldReturnHeistById() {

        when(heistRepository.findById(1L)).thenReturn(Optional.of(heist));

        when(heistMapper.mapHeistToDTO(heist)).thenReturn(createHeistDTO());

        HeistDTO expected = heistService.getHeistById(1L);

        assertThat(expected).isEqualTo(createHeistDTO());

    }

    @Test
    void shouldReturnHeistStatus() {

        when(heistRepository.findById(1L)).thenReturn(Optional.of(heist));

        when(modelMapper.map(heist, HeistStatusDTO.class)).thenReturn(createHeistStatusDTO());

        HeistStatusDTO expected = heistService.getHeistStatus(1L);

        assertThat(expected).isEqualTo(createHeistStatusDTO());

    }

    @Test
    void shouldReturnHeistOutcome() {

        Heist finishedHeist = new Heist(1L, "European Central Bank", "Frankfurt, Germany", LocalDateTime.now(),
                LocalDateTime.now().plusDays(5), HeistStatusEnum.FINISHED, HeistOutcomeEnum.SUCCEEDED, null, null);

        when(heistRepository.findById(1L)).thenReturn(Optional.of(finishedHeist));

        when(modelMapper.map(finishedHeist, HeistOutcomeDTO.class)).thenReturn(createHeistOutcomeDTO());

        HeistOutcomeDTO expected = heistService.getHeistOutcome(1L);

        assertThat(expected).isEqualTo(createHeistOutcomeDTO());

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
