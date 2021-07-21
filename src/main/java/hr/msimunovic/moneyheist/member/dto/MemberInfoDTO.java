package hr.msimunovic.moneyheist.member.dto;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDTO {
    private Long id;
    private String name;
    private MemberStatusEnum status;
}
