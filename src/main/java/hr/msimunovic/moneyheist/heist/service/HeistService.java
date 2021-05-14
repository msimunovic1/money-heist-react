package hr.msimunovic.moneyheist.heist.service;

import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.HeistDTO;

public interface HeistService {

    Heist saveHeist(HeistDTO heistDTO);
}
