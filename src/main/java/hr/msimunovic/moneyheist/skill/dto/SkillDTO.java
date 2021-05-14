package hr.msimunovic.moneyheist.skill.dto;

import hr.msimunovic.moneyheist.skill.Skill;
import lombok.Data;

import java.util.List;

@Data
public class SkillDTO {

    private List<Skill> skills;
    private String mainSkill;

}
