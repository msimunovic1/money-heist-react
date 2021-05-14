package hr.msimunovic.moneyheist.heist.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.HeistDTO;
import hr.msimunovic.moneyheist.heist.repository.HeistRepository;
import hr.msimunovic.moneyheist.skill.Skill;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;
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

    private Heist mapDTOToHeist(HeistDTO heistDTO) {

        Heist heist = new Heist();
        heist.setName(heistDTO.getName());
        heist.setLocation(heistDTO.getLocation());
        heist.setStartTime(heistDTO.getStartTime());
        heist.setEndTime(heistDTO.getEndTime());

        heistDTO.getSkills()
                .forEach(skill -> heist.addSkill(modelMapper.map(skill, Skill.class), skill.getMembers()));

        return heist;
    }
}
