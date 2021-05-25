package hr.msimunovic.moneyheist.member.mapper;

import hr.msimunovic.moneyheist.heist.dto.HeistMemberDTO;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.memberSkill.MemberSkill;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MemberMapper {

    private final SkillMapper skillMapper;

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

    public HeistMemberDTO mapMemberToHeistMemberDTO(Member member) {

        HeistMemberDTO heistMemberDTO = new HeistMemberDTO();

        // set member name
        heistMemberDTO.setName(member.getName());

        List<SkillDTO> skillDTOList = new ArrayList<>();

        for(MemberSkill ms : member.getSkills()) {
            // map member skill to DTO
            SkillDTO skillDTO = skillMapper.mapMemberSkillToDTO(ms);
            // add member skill to list
            skillDTOList.add(skillDTO);
        }

        // set member skills
        heistMemberDTO.setSkills(skillDTOList);

        return heistMemberDTO;

    }

    public Member mapDTOToMember(MemberDTO memberDTO) {

        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setSex(memberDTO.getSex());
        member.setEmail(memberDTO.getEmail());
        member.setStatus(memberDTO.getStatus());

        return member;

    }

}
