package hr.msimunovic.moneyheist.member.dto;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import lombok.Data;

import java.util.List;

/**
 * Data Transformation Object for work with API-s that request or response general Member data.
 */
@Data
public class MemberDTO {

    private String name;
    private String sex;
    private String email;
    private List<SkillDTO> skills;
    private String mainSkill;
    private MemberStatusEnum status;

}
