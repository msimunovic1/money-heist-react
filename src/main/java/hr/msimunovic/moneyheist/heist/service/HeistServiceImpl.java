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
import hr.msimunovic.moneyheist.member_skill.member_skill_improvement.MemberSkillImprovement;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.skill.mapper.SkillMapper;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
import hr.msimunovic.moneyheist.util.HeistUtil;
import hr.msimunovic.moneyheist.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
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
    private final ThreadPoolTaskScheduler taskScheduler;
    private final PeriodicTrigger periodicTrigger;
    private final MemberSkillImprovement memberSkillImprovement;

    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();

    @Override
    @Transactional
    public Heist saveHeist(HeistDTO heistDTO) {

        Heist heistFromDb = heistRepository.findByName(heistDTO.getName());

        if (heistFromDb != null) {
            throw new BadRequestException(Constants.MSG_HEIST_EXISTS);
        }

        // check is provided multiple skills with same name
        multipleSkillNameAndLevelValidator(heistDTO.getSkills());

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
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_SHOULD_NOT_BE_READY);
        }

        Set<HeistSkillDTO> skillDuplicates = new HashSet<>();
        for(HeistSkillDTO heistSkillDTO : heistSkillsDTO.getSkills()) {

            // check is provided multiple skills with same name and level
            if (!skillDuplicates.add(heistSkillDTO)) {
                throw new BadRequestException(Constants.MSG_DUPLICATED_SKILLS);
            }

            // check does skill exists in DB
            Skill skillFromDB = skillRepository.findByNameIgnoreCaseAndLevel(heistSkillDTO.getName(), heistSkillDTO.getLevel());
            if(skillFromDB==null) {
                // add if skill does not exists in DB
                heist.addSkill(modelMapper.map(heistSkillDTO, Skill.class), heistSkillDTO.getMembers());
            } else {
                heist.addSkill(skillFromDB, heistSkillDTO.getMembers());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MembersEligibleForHeistDTO getMembersEligibleForHeist(Long heistId) {

        Heist heist = findHeistById(heistId);

        Set<HeistMemberDTO> heistMemberDTOList = new HashSet<>();

        // iterate on heist skills
        for(HeistSkill heistSkill : heist.getSkills()) {

            // find skills which level is equal or greater than heist required skill
            List<Skill> skillsLevelEqualsOrGreater =
                    skillRepository.findByNameAndLevelIsGreaterOrEqual(heistSkill.getSkill().getName(), heistSkill.getSkill().getLevel().length());

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

        return MembersEligibleForHeistDTO.builder()
                .skills(heistSkillDTOList)
                .members(heistMemberDTOList)
                .build();
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
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_SHOULD_NOT_BE_PLANNING);
        }

        return heistMapper.mapHeistMembersToDTO(heist.getMembers());
    }

    @Override
    @Transactional(readOnly = true)
    public HeistOutcomeDTO getHeistOutcome(Long heistId) {

        Heist heist = findHeistById(heistId);

        if(!heist.getStatus().equals(HeistStatusEnum.FINISHED)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_SHOULD_BE_FINISHED);
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

        heistRepository.save(heist);

        // send email to members - request waiting response !!!!!!
        /*updatedHeist.getMembers().stream()
                .map(HeistMember::getMember)
                .forEach(member ->
                        emailService.sendEmail(member.getEmail(), Constants.MAIL_HEIST_PARTICIPANT_SUBJECT, Constants.MAIL_HEIST_PARTICIPANT_TEXT));*/

    }

    @Override
    public void startHeist(Long heistId) {

        log.info("Heist with id {} started.", heistId);

        Heist heist = findHeistById(heistId);

        if(!heist.getStatus().equals(HeistStatusEnum.READY)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_SHOULD_BE_READY);
        }

        // set new status to heist
        heist.setStatus(HeistStatusEnum.IN_PROGRESS);

        // save heist with new status
        Heist startedHeist = heistRepository.save(heist);

        // send email to members - request waiting response !!!!!!
        /*startedHeist.getMembers().stream()
                .map(HeistMember::getMember)
                .forEach(member ->
                        emailService.sendEmail(member.getEmail(), Constants.MAIL_HEIST_START_SUBJECT, Constants.MAIL_HEIST_START_TEXT));*/

        ScheduledFuture<?> future = taskScheduler.schedule(() -> memberSkillImprovement.increaseMemberSkills(startedHeist), periodicTrigger);
        scheduledTasks.put(startedHeist.getId(), future);

    }

    @Override
    public void endHeist(Long heistId) {

        scheduledTasks.forEach((k, v) -> {
            if (k.equals(heistId)) {
                v.cancel(true);
            }
        });

        log.info("finishing heist with id {}", heistId);

        Heist heist = findHeistById(heistId);

        // set new status to heist
        heist.setStatus(HeistStatusEnum.FINISHED);

        Set<HeistMember> heistMembers = heist.getMembers();

        // get required members from heist_skill.members
        long requiredMembers = heist.getSkills().stream()
                .mapToLong(HeistSkill::getMembers)
                .sum();

        // get participated members from heist_member
        long participatedMembers = heistMembers.stream()
                .map(HeistMember::getMember)
                .count();

        // calculate outcome result in percents
        long outcomeResult = HeistUtil.calculateOutcomeInPercents(requiredMembers, participatedMembers);

        HeistOutcomeEnum heistOutcome = HeistOutcomeEnum.FAILED;

        if(outcomeResult < 50) {
            // all members EXPIRED or INCARCERATED
            updateMembersStatus(heistMembers, List.of(MemberStatusEnum.EXPIRED, MemberStatusEnum.INCARCERATED), heistMembers.size());
        } else if(outcomeResult >= 50 && outcomeResult < 75) {

            // generate random member statuses
            List<MemberStatusEnum> memberStatuses = updateMembersStatus(heistMembers, List.of(MemberStatusEnum.values()), heistMembers.size());

            // calculate repercussion result with random generated member statuses
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
        Heist finishedHeist = heistRepository.save(heist);

        log.info("Heist with id {} finished with outcome {}.", finishedHeist.getId(), finishedHeist.getOutcome());

        // send email to members - request waiting response !!!!!!
       /* finishedHeist.getMembers().stream()
                .map(HeistMember::getMember)
                .forEach(member ->
                        emailService.sendEmail(member.getEmail(), Constants.MAIL_HEIST_FINISH_SUBJECT, Constants.MAIL_HEIST_FINISH_TEXT));*/

    }

    @Override
    public void scheduleStartEndHeist(Heist heist) {

        Date startTime = Date.from(heist.getStartTime().atZone(ZoneId.systemDefault())
                .toInstant());

        Date endTime = Date.from(heist.getEndTime().atZone(ZoneId.systemDefault())
                .toInstant());

        taskScheduler.schedule(() -> startHeist(heist.getId()), startTime);

        taskScheduler.schedule(() -> endHeist(heist.getId()), endTime);

    }

    @Override
    @Transactional(readOnly = true)
    public List<HeistInfoDTO> getAllHeists() {
        return heistRepository.findAll().stream()
                .map(heist -> modelMapper.map(heist, HeistInfoDTO.class))
                .collect(Collectors.toList());
    }

    private Heist findHeistById(Long heistId) {
        return heistRepository.findById(heistId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_HEIST_NOT_FOUND));
    }

    private boolean validateMemberHeists(Set<HeistMember> heistMembers) {

        for (HeistMember heistMember : heistMembers) {
            if(heistMember.getHeist().getStatus().equals(HeistStatusEnum.READY) || heistMember.getHeist().getStatus().equals(HeistStatusEnum.IN_PROGRESS)) {
                return false;
                /*throw new MethodNotAllowedException(Constants.MSG_MEMBERS_CONFIRMED);*/
            }
        }

        return true;

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

    public void validateStartingHeist(Heist heist) {
        if(!heist.getStatus().equals(HeistStatusEnum.PLANNING)) {
            throw new MethodNotAllowedException(Constants.MSG_HEIST_STATUS_SHOULD_BE_PLANNING);
        }
    }

    public void validatePlanningHeist(Heist heist) {

        LocalDateTime startDate = heist.getStartTime();
        LocalDateTime endDate = heist.getEndTime();
        LocalDateTime currentTime = LocalDateTime.now();

        Heist heistFromDB = heistRepository.findByNameIgnoreCase(heist.getName());

        // check does heist with the same name already exists
        if(heistFromDB != null) {
            throw new BadRequestException(Constants.MSG_HEIST_EXISTS);
        }

        // check is the startTime after the endTime or is the endTime in the past
        if (startDate!=null && endDate!=null && (startDate.isAfter(endDate) || endDate.isBefore(currentTime))) {
            throw new BadRequestException(Constants.MSG_INCORRECT_DATE_TIME);
        }

    }

    public void validateMember(Member member) {
        if(!member.getStatus().equals(MemberStatusEnum.AVAILABLE) && !member.getStatus().equals(MemberStatusEnum.RETIRED)) {
            throw new BadRequestException(Constants.MSG_MEMBER_STATUS_NOT_MATCH_TO_HEIST);
        }

        // check does member already confirmed of another heist happening at the same time
        List<Heist> readyHeists = heistRepository.findByStatusOrStatus(HeistStatusEnum.READY, HeistStatusEnum.IN_PROGRESS);
        for (Heist heist : readyHeists) {

            Member memberConfirmed = heist.getMembers().stream()
                    .map(HeistMember::getMember)
                    .filter(heistMember -> heistMember.getId().equals(member.getId()))
                    .findAny()
                    .orElse(null);

            if (memberConfirmed!=null) {
                throw new BadRequestException(Constants.MSG_MEMBER_CONFIRMED);
            }
        }
    }

    private void multipleSkillNameAndLevelValidator(List<HeistSkillDTO> skills) {

        Set<SkillDTO> skillDuplicates = new HashSet<>();
        for(HeistSkillDTO heistSkillDTO : skills) {

            SkillDTO skillDTO = SkillDTO.builder()
                    .name(heistSkillDTO.getName())
                    .level(heistSkillDTO.getLevel())
                    .build();

            // check is provided multiple skills with same name and level
            if (!skillDuplicates.add(skillDTO)) {
                throw new BadRequestException(Constants.MSG_DUPLICATED_SKILLS);
            }
        }
    }

}
