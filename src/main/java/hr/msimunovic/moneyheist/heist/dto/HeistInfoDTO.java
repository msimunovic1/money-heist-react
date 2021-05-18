package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import lombok.Data;

@Data
public class HeistInfoDTO {

    private Long id;
    private String name;
    private HeistStatusEnum status;
}
