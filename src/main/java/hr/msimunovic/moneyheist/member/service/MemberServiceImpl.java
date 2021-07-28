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
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
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
import java.util.concurrent.atomic.AtomicBoolean;
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
    public Long saveMember(MemberDTO memberDTO) {

        var memberFromDB = memberRepository.findByEmail(memberDTO.getEmail());

        if(memberFromDB != null) {
            throw new BadRequestException(Constants.MSG_MEMBER_EXISTS);
        }

        // check is provided multiple skills with same name
        multipleSkillNameValidator(memberDTO.getSkills(), memberDTO.getMainSkill());

        var member = memberMapper.mapDTOToMember(memberDTO);

        addMemberSkills(member, memberDTO.getSkills(), memberDTO.getMainSkill());

        // send email to member - request waiting response !!!!!!
        /*emailService.sendEmail(member.getEmail(), Constants.MAIL_MEMBER_ADDED_SUBJECT, Constants.MAIL_MEMBER_ADDED_TEXT);*/

        return memberRepository.save(member).getId();
    }

    @Override
    @Transactional
    public void updateSkills(Long memberId, MemberSkillDTO memberSkillDTO) {

        var member = findMemberById(memberId);

        // check is provided multiple skills with same name
        multipleSkillNameValidator(memberSkillDTO.getSkills(), memberSkillDTO.getMainSkill());

        addMemberSkills(member, memberSkillDTO.getSkills(), memberSkillDTO.getMainSkill());

    }

    @Override
    @Transactional
    public void deleteSkill(Long memberId, String skillName) {

        var member = findMemberById(memberId);

        // check does skill exists in DB
        List<Skill> skills = skillRepository.findByName(skillName);
        // throw exception if skill does not exists in DB
        if(skills.isEmpty()) {
            throw new NotFoundException(Constants.MSG_SKILL_NOT_FOUND);
        }

        if (member.getSkills()!=null) {
            List<MemberSkill> memberSkills = new ArrayList<>(member.getSkills());
            for (MemberSkill memberSkill : memberSkills) {
                // remove skill if skill exists in DB
                if (memberSkill.getSkill().getName().equals(skillName)) {
                    member.removeSkills(memberSkill.getSkill());
                }
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

    public void addMemberSkills(Member member, List<SkillDTO> skillDTOList, String mainSkill) {

        // if skill array is empty and main skill is provided
        if((skillDTOList==null || skillDTOList.isEmpty()) && !mainSkill.isEmpty()) {

            // check member’s previous skill array
            var existedMemberSkill = member.getSkills().stream()
                    .map(MemberSkill::getSkill)
                    .filter(skill -> skill.getName().equals(mainSkill))
                    .findAny()
                    .orElse(null);

            if (existedMemberSkill!=null) {
                member.addSkill(existedMemberSkill, mainSkill);
            } else {
                // check skill in DB
                var skillFromDB = skillRepository.findByNameIgnoreCaseAndLevel(mainSkill, Constants.DEFAULT_SKILL_LEVEL);
                if(skillFromDB!=null) {
                    member.addSkill(skillFromDB, mainSkill);
                } else {
                    var skill = new Skill();
                    skill.setName(mainSkill);
                    skill.setLevel(Constants.DEFAULT_SKILL_LEVEL);
                    member.addSkill(skill, mainSkill);
                }
            }
        } else {
            // if skill array is provided loop over it
            for (SkillDTO skillDTO : skillDTOList) {
                if (skillDTO.getLevel() == null) {
                    skillDTO.setLevel(Constants.DEFAULT_SKILL_LEVEL);
                }
                // check skill in DB
                var skillFromDB = skillRepository.findByNameIgnoreCaseAndLevel(skillDTO.getName(), skillDTO.getLevel());
                if(skillFromDB==null) {
                    member.addSkill(modelMapper.map(skillDTO, Skill.class), mainSkill);
                } else {
                    member.addSkill(skillFromDB, mainSkill);
                }
            }
        }
    }

    public void multipleSkillNameValidator(List<SkillDTO> memberSkills, String mainSkill) {

        var mainSkillReferencesSkills = new AtomicBoolean(false);

        Set<String> skillNameDuplicates = new HashSet<>();

        if (memberSkills != null && !memberSkills.isEmpty()) {
            memberSkills.forEach(skillDTO -> {
                        if (!skillNameDuplicates.add(skillDTO.getName())) {
                            throw new BadRequestException(Constants.MSG_DUPLICATED_SKILLS);
                        }

                        // if skill name and mainSkill are equals set flag mainSkillReferencesSkills to true
                        if (skillDTO.getName().equals(mainSkill)) {
                            mainSkillReferencesSkills.set(true);
                        }
                    });

            if (!mainSkill.isEmpty() && !mainSkillReferencesSkills.get()) {
                throw new BadRequestException(Constants.MSG_MAIN_SKILL_NOT_REFERENCES_SKILLS);
            }
        }

    }

}
