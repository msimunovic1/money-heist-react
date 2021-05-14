package hr.msimunovic.moneyheist.member.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.MemberDTO;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member saveMember(MemberDTO memberDTO) {

        Member memberFromDB = memberRepository.findByEmail(memberDTO.getEmail());

        if(memberFromDB != null) {
            throw new BadRequestException("Member with username " + memberDTO.getEmail() + " already exists.");
        }

        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setSex(memberDTO.getSex());
        member.setEmail(memberDTO.getEmail());
        member.setStatus(memberDTO.getStatus());

        memberDTO.getSkills()
                .forEach(skill -> member.addSkill(skill, memberDTO.getMainSkill()));


        return memberRepository.save(member);
    }
}
