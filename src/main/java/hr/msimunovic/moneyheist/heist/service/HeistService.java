package hr.msimunovic.moneyheist.heist.service;

import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.*;
import hr.msimunovic.moneyheist.heist_member.dto.MembersEligibleForHeistDTO;

import java.util.List;

public interface HeistService {

    Heist saveHeist(HeistDTO heistDTO);

    MembersEligibleForHeistDTO getMembersEligibleForHeist(Long heistId);

    HeistDTO getHeistById(Long heistId);

    HeistStatusDTO getHeistStatus(Long heistId);

    List<HeistSkillDTO> getHeistSkills(Long heistId);

    List<HeistMemberDTO> getHeistMembers(Long heistId);

    HeistOutcomeDTO getHeistOutcome(Long heistId);

    void saveHeistMembers(Long heistId, HeistMembersDTO heistMembersDTO);

    void startHeist(Long heistId);

    void endHeist(Long heistId);

    void updateSkills(Long heistId, HeistSkillsDTO heistSkillsDTO);

    void scheduleStartEndHeist(Heist heist);
}
