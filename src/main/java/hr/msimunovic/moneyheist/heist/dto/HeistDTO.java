package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.heist_skill.dto.HeistSkillDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HeistDTO {

    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<HeistSkillDTO> skills;

}
