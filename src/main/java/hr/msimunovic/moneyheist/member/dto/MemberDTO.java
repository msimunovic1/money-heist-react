package hr.msimunovic.moneyheist.member.dto;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.validator.MemberSkill;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Data Transformation Object for work with API-s that request or response general Member data.
 */
@Data
public class MemberDTO {

    @NotEmpty(message = "Member name is required")
    private String name;

    @NotEmpty(message = "Sex is required")
    private String sex;

    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "At least one skill is required")
    private List<SkillDTO> skills;

    private String mainSkill;

    @NotNull(message = "Status is required")
    private MemberStatusEnum status;

}
