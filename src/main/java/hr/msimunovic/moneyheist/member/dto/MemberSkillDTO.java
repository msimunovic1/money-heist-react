package hr.msimunovic.moneyheist.member.dto;

import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import lombok.Data;

import java.util.List;

/**
 * Data Transformation Object for work with API-s that request or response MEMBER_SKILL data.
 */
@Data
public class MemberSkillDTO {

    private List<SkillDTO> skills;
    private String mainSkill;

}
