package hr.msimunovic.moneyheist.heist.mapper;

import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistMemberDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import hr.msimunovic.moneyheist.heist_member.HeistMember;
import hr.msimunovic.moneyheist.heist_skill.HeistSkill;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
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

        HeistDTO heistDTO = new HeistDTO();
        heistDTO.setName(heist.getName());
        heistDTO.setLocation(heist.getLocation());
        heistDTO.setStartTime(heist.getStartTime());
        heistDTO.setEndTime(heist.getEndTime());

        List<HeistSkillDTO> heistSkillDTOList = heist.getSkills().stream()
                .map(heistSkill -> skillMapper.mapHeistSkillToDTO(heistSkill))
                .collect(Collectors.toList());

        heistDTO.setSkills(heistSkillDTOList);
        heistDTO.setStatus(heist.getStatus());

        return heistDTO;

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
