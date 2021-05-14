package hr.msimunovic.moneyheist.member_heist.dto;

import hr.msimunovic.moneyheist.heist_skill.dto.HeistSkillDTO;
import lombok.Data;

import java.util.List;

@Data
public class MembersEligibleForHeistDTO {

    List<HeistSkillDTO> skills;
    List<MembersEligibleDTO> members;
}
