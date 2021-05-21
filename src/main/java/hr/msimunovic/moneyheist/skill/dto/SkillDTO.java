package hr.msimunovic.moneyheist.skill.dto;

import lombok.Data;

/**
 * Data Transformation Object for work with API-s that request or response general Skill data.
 */
@Data
public class SkillDTO {

    private String name;
    private String level;

}
