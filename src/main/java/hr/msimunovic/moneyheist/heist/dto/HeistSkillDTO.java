package hr.msimunovic.moneyheist.heist.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Data Transformation Object for work with API-s that request or response Heist_Skill data.
 */
@Data
public class HeistSkillDTO {

    @NotEmpty(message = "Heist name field is required")
    private String name;

    @NotEmpty(message = "Level field is required")
    private String level;

    @NotEmpty(message = "Member field is required")
    private Integer members;

}
