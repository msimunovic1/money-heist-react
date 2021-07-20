package hr.msimunovic.moneyheist.member.dto;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoDTO {
    private Long id;
    private String name;
    private MemberStatusEnum status;
}
