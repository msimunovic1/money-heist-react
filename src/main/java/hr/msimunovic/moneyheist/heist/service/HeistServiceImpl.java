package hr.msimunovic.moneyheist.heist.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.heist.repository.HeistRepository;
import hr.msimunovic.moneyheist.member_heist.dto.MembersEligibleForHeistDTO;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;
    private final SkillService skillService;
    private final ModelMapper modelMapper;

    @Override
    public Heist saveHeist(HeistDTO heistDTO) {

        Heist heistFromDB = heistRepository.findByName(heistDTO.getName());

        // TODO: return 404 when the startTime is after the endTime
        // TODO: return 404 when the endTime is in the past
        // TODO: return 404 when multiple skills with the same name and level were provided.

        if(heistFromDB != null) {
            throw new BadRequestException(Constants.MSG_HEIST_EXISTS);
        }

        return heistRepository.save(mapDTOToHeist(heistDTO));
    }

    @Override
    public MembersEligibleForHeistDTO getMembersEligibleForHeist(Long heistId) {

        Heist heist = heistRepository.findById(heistId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_HEIST_NOT_FOUND));

        // TODO: return 405 when the heist members have already been confirmed

        List<Skill> skills = new ArrayList<>();

        heist.getSkills().forEach(heistSkill -> skills.add(modelMapper.map(heistSkill, Skill.class)));
        return null;
    }

    private Heist mapDTOToHeist(HeistDTO heistDTO) {

        Heist heist = new Heist();
        heist.setName(heistDTO.getName());
        heist.setLocation(heistDTO.getLocation());
        heist.setStartTime(heistDTO.getStartTime());
        heist.setEndTime(heistDTO.getEndTime());

        heistDTO.getSkills()
                .forEach(skill -> {

                    Skill skillFromDB = skillService.checkSkillInDB(skill.getName(), skill.getLevel());

                    if(skillFromDB != null) {
                        heist.addSkill(skillFromDB, skill.getMembers());
                    } else {
                        heist.addSkill(modelMapper.map(skill, Skill.class), skill.getMembers());
                    }
                });

        return heist;
    }
}
