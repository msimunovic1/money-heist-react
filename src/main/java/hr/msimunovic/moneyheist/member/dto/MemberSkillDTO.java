package hr.msimunovic.moneyheist.member.dto;

import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.validator.MemberSkill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
<<<<<<< HEAD
import lombok.NoArgsConstructor;
=======
import org.springframework.lang.Nullable;
>>>>>>> front_fixes

import javax.validation.Valid;
import java.util.List;

/**
 * Data Transformation Object for work with API-s that request or response MEMBER_SKILL data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MemberSkill(message = "Required is to enter a group of skills or a main skill.")
public class MemberSkillDTO {

    @Valid
    private List<SkillDTO> skills;

    private String mainSkill;

}
