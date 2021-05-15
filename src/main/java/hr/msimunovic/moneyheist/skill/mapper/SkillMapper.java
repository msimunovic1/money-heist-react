package hr.msimunovic.moneyheist.skill.mapper;

import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import hr.msimunovic.moneyheist.heist_skill.HeistSkill;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
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

        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setName(memberSkill.getSkill().getName());
        skillDTO.setLevel(memberSkill.getSkill().getLevel());

        return skillDTO;
    }

    public Skill mapMemberSkillToSkill(MemberSkill memberSkill) {

        Skill skill = new Skill();
        skill.setName(memberSkill.getSkill().getName());
        skill.setLevel(memberSkill.getSkill().getLevel());

        return skill;
    }

    public Skill mapSkillDTOToSkill(SkillDTO skillDTO) {

        Skill skill = new Skill();
        skill.setName(skillDTO.getName());
        skill.setLevel(skillDTO.getLevel());

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
                .map(heistSkill -> mapHeistSkillToDTO(heistSkill))
                .collect(Collectors.toList());
    }

    public HeistSkillDTO mapHeistSkillToDTO(HeistSkill heistSkill) {

        HeistSkillDTO heistSkillDTO = new HeistSkillDTO();
        heistSkillDTO.setName(heistSkill.getSkill().getName());
        heistSkillDTO.setLevel(heistSkill.getSkill().getLevel());
        heistSkillDTO.setMembers(heistSkill.getMembers());

        return heistSkillDTO;
    }
}
