package hr.msimunovic.moneyheist.heist_member.dto;

import hr.msimunovic.moneyheist.heist.dto.HeistMemberDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembersEligibleForHeistDTO {

    List<HeistSkillDTO> skills;
    Set<HeistMemberDTO> members;
}
