package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeistMemberDTO {

    private Long id;
    private String name;
    private List<SkillDTO> skills;
}
