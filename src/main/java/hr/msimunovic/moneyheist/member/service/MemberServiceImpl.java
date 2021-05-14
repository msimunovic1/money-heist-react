package hr.msimunovic.moneyheist.member.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.MemberDTO;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.SkillDTO;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Member saveMember(MemberDTO memberDTO) {

        Member memberFromDB = memberRepository.findByEmail(memberDTO.getEmail());

        // TODO: return 404 when multiple skills having the same name were provided.

        if(memberFromDB != null) {
            throw new BadRequestException(Constants.MSG_MEMBER_EXISTS);
        }

        return memberRepository.save(mapDTOToMember(memberDTO));
    }

    @Override
    @Transactional
    public void updateSkills(Long memberId, SkillDTO skillDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND));

        String mainSkill = skillDTO.getMainSkill();

        List<Skill> skills = new ArrayList<>();

        member.getSkills().forEach(memberSkill -> {
                    Skill s = new Skill();
                    s.setName(memberSkill.getSkill().getName());
                    s.setLevel(memberSkill.getSkill().getLevel());
                    skills.add(s);
                });

        List<Skill> allSkills = Stream.of(skills, skillDTO.getSkills())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());


        allSkills.forEach(skill -> member.addSkill(skill, mainSkill));

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteSkill(Long memberId, String skillName) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND));

        member.removeSkill(skillName);
    }

    private Member mapDTOToMember(MemberDTO memberDTO) {

        String mainSkill = memberDTO.getMainSkill();

        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setSex(memberDTO.getSex());
        member.setEmail(memberDTO.getEmail());
        member.setStatus(memberDTO.getStatus());

        if(memberDTO.getSkills() == null && !mainSkill.isEmpty()) {
            Skill skill = new Skill();
            skill.setName(mainSkill);
            member.addSkill(modelMapper.map(skill, Skill.class), mainSkill);
        } else {
            memberDTO.getSkills()
                    .forEach(skill -> member.addSkill(modelMapper.map(skill, Skill.class), mainSkill));
        }

        return member;
    }
}
