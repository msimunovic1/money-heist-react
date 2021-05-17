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
import hr.msimunovic.moneyheist.member.mapper.MemberMapper;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;
    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final HeistMapper heistMapper;
    private final MemberMapper memberMapper;
    private final SkillMapper skillMapper;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public Heist saveHeist(HeistDTO heistDTO) {

        Heist heist = heistMapper.mapDTOToHeist(heistDTO);

        validatePlanningHeist(heist);

        heist.setStatus(HeistStatusEnum.PLANNING);

        return heistRepository.save(heist);
    }


    @Override
    @Transactional
    public void updateSkills(Long heistId, HeistSkillsDTO heistSkillsDTO) {

        Heist heist = findHeistById(heistId);

        // check heist status
        if(heist.getStatus().equals(HeistStatusEnum.IN_PROGRESS)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_MUST_NOT_BE_PLANNING);
        }

        for(HeistSkillDTO heistSkillDTO : heistSkillsDTO.getSkills()) {
            // check does skill exists in DB
            Skill skill = skillRepository.findByNameAndLevel(heistSkillDTO.getName(), heistSkillDTO.getLevel());
            if(skill==null) {
                // add new if skill does not exists in DB
                heist.addSkill(modelMapper.map(heistSkillDTO, Skill.class), heistSkillDTO.getMembers());
            } else {
                // add skill from DB if skill exists
                heist.addSkill(skill, heistSkillDTO.getMembers());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MembersEligibleForHeistDTO getMembersEligibleForHeist(Long heistId) {

        Heist heist = findHeistById(heistId);

        MembersEligibleForHeistDTO membersEligibleForHeistDTO = new MembersEligibleForHeistDTO();
        Set<HeistMemberDTO> heistMemberDTOList = new HashSet<>();

        // iterate on heist skills
        for(HeistSkill heistSkill : heist.getSkills()) {

            // find skills which level is equal or greater than heist required skill
            List<Skill> skillsLevelEqualsOrGreater =
                    skillRepository.findByNameAndLevelIsGreaterThanEqual(heistSkill.getSkill().getName(), heistSkill.getSkill().getLevel().length());

            for(Skill skill : skillsLevelEqualsOrGreater) {

                // find members with iterated skill
                for(MemberSkill memberSkill : skill.getMembers()) {

                    Member member = memberSkill.getMember();

                    // check member status
                    if(member.getStatus().equals(MemberStatusEnum.AVAILABLE) || (member.getStatus().equals(MemberStatusEnum.RETIRED))) {

                        // if member is not in any heist or in heist with status IN_PROGRESS
                        if(member.getHeists().isEmpty() || validateMemberHeists(member.getHeists())) {
                            HeistMemberDTO heistMemberDTO = memberMapper.mapMemberToHeistMemberDTO(member);
                            // add member to list
                            heistMemberDTOList.add(heistMemberDTO);
                        }
                    }
                }
            }
        }

        // map heist skills to DTO list
        List<HeistSkillDTO> heistSkillDTOList = heist.getSkills().stream()
                .map(skillMapper::mapHeistSkillToDTO)
                .collect(Collectors.toList());

        // set heist skills
        membersEligibleForHeistDTO.setSkills(heistSkillDTOList);
        // set eligible members for heist and their skill
        membersEligibleForHeistDTO.setMembers(heistMemberDTOList);

        return membersEligibleForHeistDTO;
    }

    private boolean validateMemberHeists(Set<HeistMember> heistMembers) {

        for (HeistMember heistMember : heistMembers) {

            if(heistMember.getHeist().getStatus().equals(HeistStatusEnum.READY)) {
                throw new MethodNotAllowedException(Constants.MSG_MEMBERS_CONFIRMED);
            } else if(heistMember.getHeist().getStatus().equals(HeistStatusEnum.IN_PROGRESS)) {
                return false;
            }

        }

        return true;

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

        validateStartingHeist(heist);

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
                        log.info("heist with id {} started at {}", heist.getId(), now);
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
    public void validateStartingHeist(Heist heist) {
        // TODO: 400 (Bad Request) When one of member skills does not match at least one of the required skills of the heist
        // TODO: 400 when is already a confirmed member of another heist happening at the same time.
        if(!heist.getStatus().equals(HeistStatusEnum.PLANNING)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_MUST_BE_PLANNING);
        }
    }

    public void validatePlanningHeist(Heist heist) {

        LocalDateTime startDate = heist.getStartTime();
        LocalDateTime endDate = heist.getEndTime();
        LocalDateTime currentTime = LocalDateTime.now();

        Heist heistFromDB = heistRepository.findByName(heist.getName());

        // check does heist with the same name already exists
        if(heistFromDB != null) {
            throw new BadRequestException(Constants.MSG_HEIST_EXISTS);
        }

        // check is the startTime after the endTime or is the endTime in the past
        if (startDate.isAfter(endDate) || endDate.isBefore(currentTime)) {
            throw new BadRequestException(Constants.MSG_INCORRECT_DATE_TIME);
        }

        // TODO: check does multiple skills with the same name and level were provided

    }


    public void validateMember(Member member) {
        if(!member.getStatus().equals(MemberStatusEnum.AVAILABLE) && !member.getStatus().equals(MemberStatusEnum.RETIRED)) {
            throw new BadRequestException(Constants.MSG_MEMBER_STATUS_NOT_MATCH_TO_HEIST);
        }
    }

}
