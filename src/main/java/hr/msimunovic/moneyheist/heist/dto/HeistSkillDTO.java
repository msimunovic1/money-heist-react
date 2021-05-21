package hr.msimunovic.moneyheist.heist.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Data Transformation Object for work with API-s that request or response Heist_Skill data.
 */
@Data
public class HeistSkillDTO {

    @NotEmpty(message = "Skill name is required")
    private String name;

    @NotEmpty(message = "Level field is required")
    private String level;

    @NotNull(message = "Member field is required")
    private Integer members;

}
