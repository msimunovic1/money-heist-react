package hr.msimunovic.moneyheist.heist.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.MethodNotAllowedException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.email.service.EmailService;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.*;
import hr.msimunovic.moneyheist.heist.mapper.HeistMapper;
import hr.msimunovic.moneyheist.heist.repository.HeistRepository;
import hr.msimunovic.moneyheist.heist_member.HeistMember;
import hr.msimunovic.moneyheist.heist_member.dto.MembersEligibleForHeistDTO;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;
    private final MemberRepository memberRepository;
    private final HeistMapper heistMapper;
    private final SkillMapper skillMapper;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public Heist saveHeist(HeistDTO heistDTO) {

        Heist heistFromDB = heistRepository.findByName(heistDTO.getName());

        // TODO: return 404 when the startTime is after the endTime
        // TODO: return 404 when the endTime is in the past
        // TODO: return 404 when multiple skills with the same name and level were provided.

        if(heistFromDB != null) {
            throw new BadRequestException(Constants.MSG_HEIST_EXISTS);
        }

        Heist heist = heistMapper.mapDTOToHeist(heistDTO);
        heist.setStatus(HeistStatusEnum.PLANNING);

        return heistRepository.save(heist);
    }

    @Override
    @Transactional(readOnly = true)
    public MembersEligibleForHeistDTO getMembersEligibleForHeist(Long heistId) {

        // TODO: return 405 when the heist members have already been confirmed

        MembersEligibleForHeistDTO membersEligibleForHeistDTO = new MembersEligibleForHeistDTO();
        List<HeistMemberDTO> heistMemberDTOList = new ArrayList<>();

        Heist heist = findHeistById(heistId);

        // Find Heist by ID and Heist Skills Map to DTOs
        List<HeistSkillDTO> heistSkillDTOList = heist.getSkills().stream()
                .map(skillMapper::mapHeistSkillToDTO)
                .collect(Collectors.toList());


//        heistSkillDTOList.stream()
//                .map(heistSkillDTO -> skills.add(skillRepository.findByName(heistSkillDTO.getName())));
//
//        List<Skill> allSkills = heistSkillDTOList.stream()
//                .filter(heistSkillDTO -> skills.stream().map(skill -> skill.getLevel().length() >= heistSkillDTO.getLevel().length()))
//                .collect(Collectors::toList);
//
//        for (Skill skill: skills) {
//
//            Set<MemberSkill> memberSkills = skill.getMembers();
//            List<SkillDTO> membersEligibleSkillsDTOList = new ArrayList<>();
//
//            for (MemberSkill memberSkill: memberSkills) {
//                MembersEligibleDTO membersEligibleDTO = new MembersEligibleDTO();
//                membersEligibleDTO.setName(memberSkill.getMember().getName());
//
//                SkillDTO skillDTO = new SkillDTO();
//                skillDTO.setLevel(memberSkill.getSkill().getLevel());
//                skillDTO.setName(memberSkill.getSkill().getName());
//                membersEligibleSkillsDTOList.add(skillDTO);
//
//                membersEligibleDTO.setSkills(membersEligibleSkillsDTOList);
//
//                membersEligibleDTOList.add(membersEligibleDTO);
//            }
//        }


        membersEligibleForHeistDTO.setSkills(heistSkillDTOList);
        membersEligibleForHeistDTO.setMembers(heistMemberDTOList);

        return membersEligibleForHeistDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public HeistDTO getHeistById(Long heistId) {
        return heistMapper.mapHeistToDTO(findHeistById(heistId));
    }

    @Override
    @Transactional(readOnly = true)
    public HeistStatusDTO getHeistStatus(Long heistId) {
        return modelMapper.map(findHeistById(heistId), HeistStatusDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HeistSkillDTO> getHeistSkills(Long heistId) {
        return skillMapper.mapHeistSkillsToDTO(findHeistById(heistId).getSkills());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HeistMemberDTO> getHeistMembers(Long heistId) {

        Heist heist = findHeistById(heistId);

        if(heist.getStatus().equals(HeistStatusEnum.PLANNING)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_MUST_NOT_BE_PLANNING);
        }

        return heistMapper.mapHeistMembersToDTO(heist.getMembers());
    }

    @Override
    @Transactional(readOnly = true)
    public HeistOutcomeDTO getHeistOutcome(Long heistId) {
        return modelMapper.map(findHeistById(heistId), HeistOutcomeDTO.class);
    }

    @Override
    @Transactional
    public void saveHeistMembers(Long heistId, HeistMembersDTO heistMembersDTO) {

        Heist heist = findHeistById(heistId);

        Arrays.stream(heistMembersDTO.getMembers())
                .forEach(heistMember -> {
                    Member member = memberRepository.findByName(heistMember)
                            .orElseThrow(() -> new BadRequestException(Constants.MSG_MEMBER_NOT_FOUND));
                    validateMember(member);
                    heist.addMember(member);
                });

        validateHeist(heist);

        heist.setStatus(HeistStatusEnum.READY);

        Heist updatedHeist = heistRepository.save(heist);

        // TODO: repair this
        /*updatedHeist.getMembers()
                .forEach(heistMember ->
                        emailService.sendEmail(heistMember.getMember().getEmail(), Constants.MAIL_MEMBER_ADDED_TO_HEIST_SUBJECT, Constants.MAIL_MEMBER_ADDED_TO_HEIST_TEXT));*/

    }

    @Override
    @Transactional
    public void startHeistManually(Long heistId) {

        Heist heist = findHeistById(heistId);

        if(!heist.getStatus().equals(HeistStatusEnum.READY)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_MUST_BE_READY);
        }

        heist.setStatus(HeistStatusEnum.IN_PROGRESS);
        heistRepository.save(heist);

    }

    private Heist findHeistById(Long heistId) {
        return heistRepository.findById(heistId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_HEIST_NOT_FOUND));
    }

    private void validateHeist(Heist heist) {
        // TODO: 400 (Bad Request) When one of member skills does not match at least one of the required skills of the heist
        // TODO: 400 when is already a confirmed member of another heist happening at the same time.
        if(!heist.getStatus().equals(HeistStatusEnum.PLANNING)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_MUST_BE_PLANNING);
        }
    }

    private void validateMember(Member member) {
        if(!member.getStatus().equals(MemberStatusEnum.AVAILABLE) && !member.getStatus().equals(MemberStatusEnum.RETIRED)) {
            throw new BadRequestException(Constants.MSG_MEMBER_STATUS_NOT_MATCH_TO_HEIST);
        }
    }

}
