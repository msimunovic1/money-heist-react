package hr.msimunovic.moneyheist.heist.mapper;

import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistMemberDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import hr.msimunovic.moneyheist.heistMember.HeistMember;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import hr.msimunovic.moneyheist.skill.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class HeistMapper {

    private final SkillService skillService;
    private final SkillMapper skillMapper;
    private final ModelMapper modelMapper;

    public HeistDTO mapHeistToDTO(Heist heist) {

        List<HeistSkillDTO> heistSkillDTOList = heist.getSkills().stream()
                .map(skillMapper::mapHeistSkillToDTO)
                .collect(Collectors.toList());

        return HeistDTO.builder()
                .name(heist.getName())
                .location(heist.getLocation())
                .startTime(heist.getStartTime())
                .endTime(heist.getEndTime())
                .skills(heistSkillDTOList)
                .status(heist.getStatus())
                .build();

    }

    public Heist mapDTOToHeist(HeistDTO heistDTO) {

        Heist heist = new Heist();
        heist.setName(heistDTO.getName());
        heist.setLocation(heistDTO.getLocation());
        heist.setStartTime(heistDTO.getStartTime());
        heist.setEndTime(heistDTO.getEndTime());

        for (HeistSkillDTO heistSkillDTO : heistDTO.getSkills()) {
            if (heistSkillDTO.getLevel() == null) {
                heistSkillDTO.setLevel(Constants.DEFAULT_SKILL_LEVEL);
            }

            Skill skillFromDB = skillService.checkSkillInDB(heistSkillDTO.getName(), heistSkillDTO.getLevel());
            Integer members = heistSkillDTO.getMembers();

            if(skillFromDB != null) {
                heist.addSkill(skillFromDB, members);
            } else {
                heist.addSkill(modelMapper.map(heistSkillDTO, Skill.class), members);
            }
        }

        return heist;
    }

    public List<HeistMemberDTO> mapHeistMembersToDTO(Set<HeistMember> heistMembers) {
        return heistMembers.stream()
                .map(heistMember -> mapHeistMemberToDTO(heistMember))
                .collect(Collectors.toList());
    }

    public HeistMemberDTO mapHeistMemberToDTO(HeistMember heistMember) {

        HeistMemberDTO heistMemberDTO = new HeistMemberDTO();
        heistMemberDTO.setId(heistMember.getMember().getId());
        heistMemberDTO.setName(heistMember.getMember().getName());

        List<SkillDTO> skillDTOList = heistMember.getMember().getSkills().stream()
                .map(memberSkill -> skillMapper.mapMemberSkillToDTO(memberSkill))
                .collect(Collectors.toList());

        heistMemberDTO.setSkills(skillDTOList);

        return heistMemberDTO;
    }


}
