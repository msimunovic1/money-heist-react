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
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final SkillRepository skillRepository;
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

        for(SkillDTO skillDTO : memberSkillDTO.getSkills()) {
            Skill skill = skillRepository.findByNameAndLevel(skillDTO.getName(), skillDTO.getLevel());
            if (skill==null) {
                member.addSkill(modelMapper.map(skillDTO, Skill.class), mainSkill);
            } else {
                member.addSkill(skill, mainSkill);
            }
        }

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteSkill(Long memberId, String skillName) {

        Member member = findMemberById(memberId);

        for (MemberSkill memberSkill : member.getSkills()) {
            // check does skill exists in DB
            Skill skill = skillRepository.findByNameAndLevel(skillName, memberSkill.getSkill().getLevel());
            // throw exception if skill does not exists in DB
            if(skill == null) {
                throw new NotFoundException(Constants.MSG_SKILL_NOT_FOUND);
            }
            // remove skill if skill exists in DB
            member.removeSkill(skill);
        }

        memberRepository.save(member);


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
