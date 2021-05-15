package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import lombok.Data;

import java.util.List;

@Data
public class HeistMemberDTO {

    private String name;
    private List<SkillDTO> skills;
}
