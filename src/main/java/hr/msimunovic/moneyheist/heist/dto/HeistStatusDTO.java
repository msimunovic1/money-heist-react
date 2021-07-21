package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transformation Object for work with API-s that request or response Heist status data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeistStatusDTO {

    private HeistStatusEnum status;

}
