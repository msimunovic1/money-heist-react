package hr.msimunovic.moneyheist.member.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.email.service.EmailService;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.member.dto.MemberInfoDTO;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;
import hr.msimunovic.moneyheist.member.mapper.MemberMapper;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.memberSkill.MemberSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final MemberMapper memberMapper;
    private final SkillMapper skillMapper;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Override
    public List<MemberInfoDTO> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(member -> modelMapper.map(member, MemberInfoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Member saveMember(MemberDTO memberDTO) {

        Member memberFromDB = memberRepository.findByEmail(memberDTO.getEmail());

        if(memberFromDB != null) {
            throw new BadRequestException(Constants.MSG_MEMBER_EXISTS);
        }

        // check is provided multiple skills with same name
        multipleSkillNameValidator(memberDTO.getSkills());

        Member member = memberMapper.mapDTOToMember(memberDTO);

        // send email to member - request waiting response !!!!!!
        /*emailService.sendEmail(member.getEmail(), Constants.MAIL_MEMBER_ADDED_SUBJECT, Constants.MAIL_MEMBER_ADDED_TEXT);*/

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public void updateSkills(Long memberId, MemberSkillDTO memberSkillDTO) {

        Member member = findMemberById(memberId);

        // check is provided multiple skills with same name
        multipleSkillNameValidator(memberSkillDTO.getSkills());

        String mainSkill = memberSkillDTO.getMainSkill();

        for(SkillDTO skillDTO : memberSkillDTO.getSkills()) {

            // check does skill exists in DB
            Skill skillFromDB = skillRepository.findByNameAndLevel(skillDTO.getName(), skillDTO.getLevel());
            if(skillFromDB==null) {
                // if skill does not exists then add new skill
                member.addSkill(modelMapper.map(skillDTO, Skill.class), mainSkill);
            } else {
                member.addSkill(skillFromDB, mainSkill);
            }
        }
    }

    @Override
    @Transactional
    public void deleteSkill(Long memberId, String skillName) {

        Member member = findMemberById(memberId);

        // check does skill exists in DB
        List<Skill> skills = skillRepository.findByName(skillName);
        // throw exception if skill does not exists in DB
        if(skills.isEmpty()) {
            throw new NotFoundException(Constants.MSG_SKILL_NOT_FOUND);
        }

        List<MemberSkill> memberSkills = new ArrayList<>(member.getSkills());

        for (MemberSkill memberSkill : memberSkills) {
            // remove skill if skill exists in DB
            if(memberSkill.getSkill().getName().equals(skillName)) {
                member.removeSkills(memberSkill.getSkill());
            }
        }

        memberRepository.save(member);


    }

    @Override
    public MemberDTO getMemberById(Long memberId) {
        return memberMapper.mapMemberToDTO(findMemberById(memberId));
    }

    @Override
    public MemberSkillDTO getMemberSkills(Long memberId) {
        return skillMapper.mapMemberSkillsToDTO(findMemberById(memberId).getSkills());
    }

    public Member findMemberById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND));
    }


    public void multipleSkillNameValidator(List<SkillDTO> memberSkills) {

        Set<String> skillNameDuplicates = new HashSet<>();
        memberSkills.stream()
                .forEach(skillDTO -> {
                    if(!skillNameDuplicates.add(skillDTO.getName())) {
                        throw new BadRequestException(Constants.MSG_DUPLICATED_SKILLS);
                    }
                });

    }

}
