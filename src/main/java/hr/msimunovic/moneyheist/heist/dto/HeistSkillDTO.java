package hr.msimunovic.moneyheist.heist.dto;

import lombok.Data;

/**
 * Data Transformation Object for work with API-s that request or response Heist_Skill data.
 */
@Data
public class HeistSkillDTO {

    private String name;
    private String level;
    private Integer members;

}
