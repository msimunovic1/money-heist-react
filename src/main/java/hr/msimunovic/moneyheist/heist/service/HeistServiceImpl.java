package hr.msimunovic.moneyheist.heist.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.MethodNotAllowedException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.common.enums.HeistOutcomeEnum;
import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.email.service.EmailService;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.*;
import hr.msimunovic.moneyheist.heist.mapper.HeistMapper;
import hr.msimunovic.moneyheist.heist.repository.HeistRepository;
import hr.msimunovic.moneyheist.heist_member.HeistMember;
import hr.msimunovic.moneyheist.heist_member.dto.MembersEligibleForHeistDTO;
import hr.msimunovic.moneyheist.heist_skill.HeistSkill;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.member.service.MemberService;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import hr.msimunovic.moneyheist.util.HeistUtil;
import hr.msimunovic.moneyheist.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final HeistMapper heistMapper;
    private final SkillMapper skillMapper;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public Heist saveHeist(HeistDTO heistDTO) {

        LocalDateTime startDate = heistDTO.getStartTime();
        LocalDateTime endDate = heistDTO.getEndTime();
        LocalDateTime currentTime = LocalDateTime.now();

        Heist heistFromDB = heistRepository.findByName(heistDTO.getName());

        // check does heist with the same name already exists
        if(heistFromDB != null) {
            throw new BadRequestException(Constants.MSG_HEIST_EXISTS);
        }

        // check is the startTime after the endTime or is the endTime in the past
        if (startDate.isAfter(endDate) || endDate.isBefore(currentTime)) {
            throw new BadRequestException(Constants.MSG_INCORRECT_DATE_TIME);
        }

        // TODO: check does multiple skills with the same name and level were provided


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

        Heist heist = findHeistById(heistId);

        if(!heist.getStatus().equals(HeistStatusEnum.FINISHED)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_MUST_BE_FINISHED);
        }

        return modelMapper.map(heist, HeistOutcomeDTO.class);
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

        // set new status to heist
        heist.setStatus(HeistStatusEnum.IN_PROGRESS);

        // save heist with new status
        heistRepository.save(heist);

    }

    public Heist findHeistById(Long heistId) {
        return heistRepository.findById(heistId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_HEIST_NOT_FOUND));
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void startHeist() {

        LocalDateTime now = LocalDateTime.now();

        log.info("Scheduler started at: {}", now);

        List<Heist> heistList = heistRepository.findAll();

        heistList.stream()
                .forEach(heist -> {

                    if(heist.getStartTime().isEqual(now)) {
                        startHeistManually(heist.getId());
                    }

                    if(heist.getEndTime().isEqual(now)) {
                        endHeist(heist);
                    }

                });
    }

    public void endHeist(Heist heist) {

        // set new status to heist
        heist.setStatus(HeistStatusEnum.FINISHED);

        Set<HeistMember> heistMembers = heist.getMembers();

        long requiredMembers = heist.getSkills().stream()
                .mapToLong(HeistSkill::getMembers)
                .sum();

        long participatedMembers = heistMembers.stream()
                .map(HeistMember::getMember)
                .count();

        long outcomeResult = HeistUtil.determineOutcomeInPercents(requiredMembers, participatedMembers);

        HeistOutcomeEnum heistOutcome = HeistOutcomeEnum.FAILED;

        if(outcomeResult < 50) {

            heistOutcome = HeistOutcomeEnum.FAILED;

            updateMembersStatus(heistMembers, List.of(MemberStatusEnum.EXPIRED, MemberStatusEnum.INCARCERATED), heistMembers.size());

        } else if(outcomeResult >= 50 && outcomeResult < 75) {

            List<MemberStatusEnum> memberStatuses = updateMembersStatus(heistMembers, List.of(MemberStatusEnum.values()), heistMembers.size());

            long repercussionResult = HeistUtil.determinateHeistRepercussion(memberStatuses);

            if(repercussionResult < 70) {
                // 2/3 of the members EXPIRED or INCARCERATED
                heistOutcome = HeistOutcomeEnum.SUCCEEDED;
            } else {
                // 1/3 of the members EXPIRED or INCARCERATED
                heistOutcome = HeistOutcomeEnum.FAILED;
            }

        } else if(outcomeResult >= 75 && outcomeResult < 100) {

            heistOutcome = HeistOutcomeEnum.SUCCEEDED;

            // 1/3 of the members INCARCERATED
            long oneThirdOfMembers = Math.round((heistMembers.size() * 0.33));

            updateMembersStatus(heistMembers, List.of(MemberStatusEnum.INCARCERATED), oneThirdOfMembers);

        } else if(outcomeResult == 100) {
            heistOutcome = HeistOutcomeEnum.SUCCEEDED;
        }

        // set outcome to heist
        heist.setOutcome(heistOutcome);

        // save heist with new status and outcome result
        heistRepository.save(heist);

    }

    private List<MemberStatusEnum> updateMembersStatus(Set<HeistMember> members, List<MemberStatusEnum> possibleStatuses, long limit) {

       return members.stream()
            .map(heistMember -> memberRepository.findById(heistMember.getMember().getId())
                    .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND)))
            .limit(limit)
            .map(member -> {
                // update random generated member status
                member.setStatus(MemberUtil.determineMemberStatus(possibleStatuses));
                Member updatedMember = memberRepository.save(member);
                return updatedMember.getStatus();
            })
           .collect(Collectors.toList());

    }

    // TODO: prebaciti ovo u neku drugu klasu
    public void validateHeist(Heist heist) {
        // TODO: 400 (Bad Request) When one of member skills does not match at least one of the required skills of the heist
        // TODO: 400 when is already a confirmed member of another heist happening at the same time.
        if(!heist.getStatus().equals(HeistStatusEnum.PLANNING)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_MUST_BE_PLANNING);
        }
    }

    public void validateMember(Member member) {
        if(!member.getStatus().equals(MemberStatusEnum.AVAILABLE) && !member.getStatus().equals(MemberStatusEnum.RETIRED)) {
            throw new BadRequestException(Constants.MSG_MEMBER_STATUS_NOT_MATCH_TO_HEIST);
        }
    }

}
