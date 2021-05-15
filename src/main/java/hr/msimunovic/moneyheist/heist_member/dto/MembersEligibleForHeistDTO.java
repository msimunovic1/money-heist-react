package hr.msimunovic.moneyheist.heist_member.dto;

import hr.msimunovic.moneyheist.heist.dto.HeistMemberDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import lombok.Data;

import java.util.List;

@Data
public class MembersEligibleForHeistDTO {

    List<HeistSkillDTO> skills;
    List<HeistMemberDTO> members;
}
