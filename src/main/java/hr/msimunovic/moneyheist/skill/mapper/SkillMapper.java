package hr.msimunovic.moneyheist.skill.mapper;

import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import hr.msimunovic.moneyheist.heistSkill.HeistSkill;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;
import hr.msimunovic.moneyheist.memberSkill.MemberSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class SkillMapper {

    public SkillDTO mapMemberSkillToDTO(MemberSkill memberSkill) {

        return SkillDTO.builder()
                .name(memberSkill.getSkill().getName())
                .level(memberSkill.getSkill().getLevel())
                .build();
    }

    public Skill mapMemberSkillToSkill(MemberSkill memberSkill) {

        Skill skill = new Skill();
        skill.setName(memberSkill.getSkill().getName());
        skill.setLevel(memberSkill.getSkill().getLevel());

        return skill;
    }

    public MemberSkillDTO mapMemberSkillsToDTO(Set<MemberSkill> memberSkills) {

        MemberSkillDTO memberSkillDTO = new MemberSkillDTO();

        // TODO: ovo izdvojiti u metodu
        List<SkillDTO> skillDTOList = new ArrayList<>();

        memberSkills
                .forEach(memberSkill -> {
                    if (memberSkill.getMainSkill().equals("Y")) {
                        memberSkillDTO.setMainSkill(memberSkill.getSkill().getName());
                    }
                    skillDTOList.add(mapMemberSkillToDTO(memberSkill));
                });

        memberSkillDTO.setSkills(skillDTOList);

        return memberSkillDTO;
    }

    public List<HeistSkillDTO> mapHeistSkillsToDTO(Set<HeistSkill> heistSkills) {
        return heistSkills.stream()
                .map(this::mapHeistSkillToDTO)
                .collect(Collectors.toList());
    }

    public HeistSkillDTO mapHeistSkillToDTO(HeistSkill heistSkill) {

     /*   HeistSkillDTO heistSkillDTO = new HeistSkillDTO();
        heistSkillDTO.setName(heistSkill.getSkill().getName());
        heistSkillDTO.setLevel(heistSkill.getSkill().getLevel());
        heistSkillDTO.setMembers(heistSkill.getMembers());*/

        return HeistSkillDTO.builder()
                .name(heistSkill.getSkill().getName())
                .level(heistSkill.getSkill().getLevel())
                .members(heistSkill.getMembers())
                .build();
    }
}
