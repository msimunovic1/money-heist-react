package hr.msimunovic.moneyheist.heist.service;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistOutcomeDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistStatusDTO;
import hr.msimunovic.moneyheist.heist.mapper.HeistMapper;
import hr.msimunovic.moneyheist.heist.repository.HeistRepository;
import hr.msimunovic.moneyheist.heist_member.dto.MembersEligibleForHeistDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;
    private final HeistMapper heistMapper;
    private final ModelMapper modelMapper;

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

        return heistRepository.save(heistMapper.mapDTOToHeist(heistDTO));
    }

    @Override
    @Transactional
    public MembersEligibleForHeistDTO getMembersEligibleForHeist(Long heistId) {

        Heist heist = heistRepository.findById(heistId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_HEIST_NOT_FOUND));

        // TODO: return 405 when the heist members have already been confirmed

        MembersEligibleForHeistDTO membersEligibleForHeistDTO = new MembersEligibleForHeistDTO();
        List<HeistSkillDTO> heistSkillDTOList = new ArrayList<>();

        heist.getSkills()
                .forEach(heistSkill -> {

                    HeistSkillDTO heistSkillDTO = heistMapper.mapHeistSkillToDTO(heistSkill);

                    heistSkillDTOList.add(heistSkillDTO);

                });

        membersEligibleForHeistDTO.setSkills(heistSkillDTOList);

        // TODO: preko skill id pronaci membere



        return membersEligibleForHeistDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public HeistDTO getHeistById(Long heistId) {

        Heist heist = heistRepository.findById(heistId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_HEIST_NOT_FOUND));

        return heistMapper.mapHeistToDTO(heist);
    }

    @Override
    @Transactional(readOnly = true)
    public HeistStatusDTO getHeistStatus(Long heistId) {

        Heist heist = heistRepository.findById(heistId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_HEIST_NOT_FOUND));

        return modelMapper.map(heist, HeistStatusDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HeistSkillDTO> getSkillsByHeistId(Long heistId) {

        Heist heist = heistRepository.findById(heistId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_HEIST_NOT_FOUND));

        return heistMapper.mapHeistSkillsToDTO(heist.getSkills());
    }

    @Override
    public HeistOutcomeDTO getHeistOutcome(Long heistId) {

        Heist heist = heistRepository.findById(heistId)
                .orElseThrow(() -> new NotFoundException(Constants.MSG_HEIST_NOT_FOUND));

        return modelMapper.map(heist, HeistOutcomeDTO.class);
    }


}
