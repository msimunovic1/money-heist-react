package hr.msimunovic.moneyheist.member.service;

import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;

public interface MemberService {

    Member saveMember(MemberDTO memberDTO);

    void updateSkills(Long memberId, SkillDTO skillDTO);

    void deleteSkill(Long memberId, String skillName);
}
