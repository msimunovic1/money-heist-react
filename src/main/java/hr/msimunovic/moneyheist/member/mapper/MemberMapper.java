package hr.msimunovic.moneyheist.member.mapper;

import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import hr.msimunovic.moneyheist.skill.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class MemberMapper {

    private final SkillService skillService;
    private final SkillMapper skillMapper;
    private final ModelMapper modelMapper;

    public MemberDTO mapMemberToDTO(Member member) {

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName(member.getName());
        memberDTO.setSex(member.getSex());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setStatus(member.getStatus());

        // TODO: ovo izdvojiti u metodu
        List<SkillDTO> skillDTOList = new ArrayList<>();

        member.getSkills()
            .forEach(memberSkill -> {
                if (memberSkill.getMainSkill().equals("Y")) {
                    memberDTO.setMainSkill(memberSkill.getSkill().getName());
                }
                skillDTOList.add(skillMapper.mapMemberSkillToDTO(memberSkill));
            });

        memberDTO.setSkills(skillDTOList);

        return memberDTO;
    }




    // TODO: razmotriti ovu metodu za refactor + izbaciti modelMapper
    public Member mapDTOToMember(MemberDTO memberDTO) {

        String mainSkill = memberDTO.getMainSkill();

        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setSex(memberDTO.getSex());
        member.setEmail(memberDTO.getEmail());
        member.setStatus(memberDTO.getStatus());

        // If skill array is empty and main skill is provided
        if(memberDTO.getSkills() == null && !mainSkill.isEmpty()) {

            Skill skillFromDB = skillService.checkSkillInDB(mainSkill, Constants.DEFAULT_SKILL_LEVEL);

            if(skillFromDB != null) {
                member.addSkill(skillFromDB, mainSkill);
            } else {
                Skill skill = new Skill();
                skill.setName(mainSkill);
                skill.setLevel(Constants.DEFAULT_SKILL_LEVEL);
                member.addSkill(modelMapper.map(skill, Skill.class), mainSkill);
            }


        } else {

            memberDTO.getSkills()
                    .forEach(skill -> {

                        if (skill.getLevel() == null) {
                            skill.setLevel(Constants.DEFAULT_SKILL_LEVEL);
                        }

                        Skill skillFromDB = skillService.checkSkillInDB(skill.getName(), skill.getLevel());

                        if(skillFromDB != null) {
                            member.addSkill(modelMapper.map(skillFromDB, Skill.class), mainSkill);
                        } else {
                            member.addSkill(modelMapper.map(skill, Skill.class), mainSkill);
                        }

                    });

        }

        return member;
    }

}
