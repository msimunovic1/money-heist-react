package hr.msimunovic.moneyheist.skill.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Data Transformation Object for work with API-s that request or response general Skill data.
 */
@Data
public class SkillDTO {

    @NotEmpty(message = "Skill name is required")
    private String name;

    private String level;

}
