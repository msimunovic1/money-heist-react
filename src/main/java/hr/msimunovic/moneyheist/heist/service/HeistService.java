package hr.msimunovic.moneyheist.heist.service;

import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistOutcomeDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistStatusDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import hr.msimunovic.moneyheist.heist_member.dto.MembersEligibleForHeistDTO;

import java.util.List;

public interface HeistService {

    Heist saveHeist(HeistDTO heistDTO);

    MembersEligibleForHeistDTO getMembersEligibleForHeist(Long heistId);

    HeistDTO getHeistById(Long heistId);

    HeistStatusDTO getHeistStatus(Long heistId);

    List<HeistSkillDTO> getSkillsByHeistId(Long heistId);

    HeistOutcomeDTO getHeistOutcome(Long heistId);
}
