package hr.msimunovic.moneyheist.heist.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Data Transformation Object for work with API-s that request or response HEIST_SKILL data.
 */
@Data
public class HeistSkillsDTO {

    @Valid
    @NotEmpty(message = "At least one skill is required.")
    private List<HeistSkillDTO> skills;
}
