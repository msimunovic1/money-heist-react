package hr.msimunovic.moneyheist.member.service;

import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;

public interface MemberService {

    Member saveMember(MemberDTO memberDTO);

    void updateSkills(Long memberId, MemberSkillDTO memberSkillDTO);

    void deleteSkill(Long memberId, String skillName);

    MemberDTO getMemberById(Long memberId);

    MemberSkillDTO getSkillsByMemberId(Long memberId);
}
