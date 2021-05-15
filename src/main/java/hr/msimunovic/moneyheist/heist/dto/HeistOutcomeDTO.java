package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.common.enums.HeistOutcomeEnum;
import lombok.Data;

@Data
public class HeistOutcomeDTO {

    private HeistOutcomeEnum outcome;
}
