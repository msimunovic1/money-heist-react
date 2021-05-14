package hr.msimunovic.moneyheist.heist.service;

import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.member_heist.dto.MembersEligibleForHeistDTO;

public interface HeistService {

    Heist saveHeist(HeistDTO heistDTO);

    MembersEligibleForHeistDTO getMembersEligibleForHeist(Long heistId);
}
