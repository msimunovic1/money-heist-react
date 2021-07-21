package hr.msimunovic.moneyheist.member.dto;

import hr.msimunovic.moneyheist.common.enums.MemberSexEnum;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.skill.dto.SkillDTO;
import hr.msimunovic.moneyheist.validator.MemberSex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Data Transformation Object for work with API-s that request or response general Member data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    @NotEmpty(message = "Member name is required")
    private String name;

    @NotNull(message = "Sex is required")
    @MemberSex(anyOf = {MemberSexEnum.F, MemberSexEnum.M})
    private MemberSexEnum sex;

    @NotEmpty(message = "Email is required")
    @Email(message = "Incorrect email format")
    private String email;

    @Valid
    @NotEmpty(message = "At least one skill is required")
    private List<SkillDTO> skills;

    private String mainSkill;

    @NotNull(message = "Status is required")
    private MemberStatusEnum status;

}
