package hr.msimunovic.moneyheist.member.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;
import hr.msimunovic.moneyheist.member.mapper.MemberMapper;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.skill.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public Member saveMember(MemberDTO memberDTO) {

        Member memberFromDB = memberRepository.findByEmail(memberDTO.getEmail());

        // TODO: return 404 when multiple skills having the same name were provided.

        if(memberFromDB != null) {
            throw new BadRequestException(Constants.MSG_MEMBER_EXISTS);
        }

        return memberRepository.save(memberMapper.mapDTOToMember(memberDTO));
    }

    @Override
    @Transactional
    public void updateSkills(Long memberId, MemberSkillDTO memberSkillDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND));

        String mainSkill = memberSkillDTO.getMainSkill();

        List<Skill> skills = new ArrayList<>();

        member.getSkills().forEach(memberSkill -> {
                    Skill s = new Skill();
                    s.setName(memberSkill.getSkill().getName());
                    s.setLevel(memberSkill.getSkill().getLevel());
                    skills.add(s);
                });

/*        List<Skill> allSkills = Stream.of(skills, skillDTO.getSkills())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());


        allSkills.forEach(skill -> member.addSkill(skill, mainSkill));*/

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteSkill(Long memberId, String skillName) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND));


/*        member.getSkills().stream()
                .map(memberSkill -> modelMapper.map(memberSkill, Skill.class))
                .filter(skill -> skill.getName().equals(skillName))
                .forEach(skill -> member.removeSkill(skill));*/

     /*   Set<MemberSkill> memberSkills = member.getSkills().stream()
                .filter(skill -> skill.getSkill().getName().equals(skillName))
                .collect(Collectors.toSet());*/


    }

    @Override
    @Transactional(readOnly = true)
    public MemberDTO getMemberById(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND));

        return memberMapper.mapMemberToDTO(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberSkillDTO getSkillsByMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND));

        return memberMapper.mapMemberSkillsToDTO(member.getSkills());
    }


}
