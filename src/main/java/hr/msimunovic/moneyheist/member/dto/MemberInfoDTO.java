package hr.msimunovic.moneyheist.member.dto;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import lombok.Data;

@Data
public class MemberInfoDTO {
    private Long id;
    private String name;
    private MemberStatusEnum status;
}
