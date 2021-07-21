package hr.msimunovic.moneyheist.heist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Data Transformation Object for work with API-s that request or response Heist_Skill data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeistSkillDTO {

    @NotEmpty(message = "Skill name is required")
    private String name;

    @NotEmpty(message = "Level field is required")
    @Pattern(regexp = "[\\*]*", message = "Skill level should be made of asterisk characters")
    @Size(max = 10, message = "Skill level max value is 10")
    private String level;

    @NotNull(message = "Member field is required")
    private Integer members;

}
