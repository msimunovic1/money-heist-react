package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.common.enums.HeistOutcomeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeistOutcomeDTO {

    private HeistOutcomeEnum outcome;
}
