package hr.msimunovic.moneyheist.member.service;

import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.member.dto.MemberInfoDTO;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;

import java.util.List;

public interface MemberService {

    Long saveMember(MemberDTO memberDTO);

    void updateSkills(Long memberId, MemberSkillDTO memberSkillDTO);

    void deleteSkill(Long memberId, String skillName);

    MemberDTO getMemberById(Long memberId);

    MemberSkillDTO getMemberSkills(Long memberId);

    List<MemberInfoDTO> getAllMembers();
}
