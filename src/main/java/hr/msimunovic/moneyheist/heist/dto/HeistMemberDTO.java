package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HeistMemberDTO {

    private Long id;
    private String name;
    private List<SkillDTO> skills;
}
