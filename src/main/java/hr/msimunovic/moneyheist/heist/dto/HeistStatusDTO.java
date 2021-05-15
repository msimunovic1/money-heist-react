package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import lombok.Data;

/**
 * Data Transformation Object for work with API-s that request or response Heist status data.
 */
@Data
public class HeistStatusDTO {

    private HeistStatusEnum status;

}
