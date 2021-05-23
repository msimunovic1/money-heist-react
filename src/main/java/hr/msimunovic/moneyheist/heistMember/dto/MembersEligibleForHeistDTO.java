package hr.msimunovic.moneyheist.heistMember.dto;

import hr.msimunovic.moneyheist.heist.dto.HeistMemberDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class MembersEligibleForHeistDTO {

    List<HeistSkillDTO> skills;
    Set<HeistMemberDTO> members;
}
