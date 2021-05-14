package hr.msimunovic.moneyheist.member;

import hr.msimunovic.moneyheist.common.MemberStatusEnum;
import hr.msimunovic.moneyheist.heist_skill.HeistSkillDTO;
import lombok.Data;

import java.util.List;

@Data
public class MemberDTO {

    private String name;
    private String sex;
    private String email;
    private List<HeistSkillDTO> skills;
    private String mainSkill;
    private MemberStatusEnum status;

}
