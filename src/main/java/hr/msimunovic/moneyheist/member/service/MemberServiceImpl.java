package hr.msimunovic.moneyheist.member.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.email.service.EmailService;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;
import hr.msimunovic.moneyheist.member.mapper.MemberMapper;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final SkillMapper skillMapper;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public Member saveMember(MemberDTO memberDTO) {

        Member memberFromDB = memberRepository.findByEmail(memberDTO.getEmail());

        // TODO: return 404 when multiple skills having the same name were provided.

        if(memberFromDB != null) {
            throw new BadRequestException(Constants.MSG_MEMBER_EXISTS);
        }

        Member member = memberMapper.mapDTOToMember(memberDTO);

    //    emailService.sendEmail(member.getEmail(), Constants.MAIL_MEMBER_ADDED_TO_HEIST_SUBJECT, Constants.MAIL_MEMBER_ADDED_TO_HEIST_TEXT);

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public void updateSkills(Long memberId, MemberSkillDTO memberSkillDTO) {

        Member member = findMemberById(memberId);

        String mainSkill = memberSkillDTO.getMainSkill();

        List<Skill> oldSkills = member.getSkills().stream()
                .map(memberSkill -> skillMapper.mapMemberSkillToSkill(memberSkill))
                .collect(Collectors.toList());

        memberSkillDTO.getSkills().stream()
                .map(skill -> skillMapper.mapSkillDTOToSkill(skill))
                .forEach(newSkill -> {

                    oldSkills.forEach(oldSkill -> {
                        if (oldSkill.getName().equals(newSkill.getName())) {
                            throw new BadRequestException("fdgadf");
                        }
                    });

                    member.addSkill(newSkill, mainSkill);

                }
            );

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteSkill(Long memberId, String skillName) {

        Member member = findMemberById(memberId);


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
        return memberMapper.mapMemberToDTO(findMemberById(memberId));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberSkillDTO getMemberSkills(Long memberId) {
        return skillMapper.mapMemberSkillsToDTO(findMemberById(memberId).getSkills());
    }

    public Member findMemberById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND));
    }

}
