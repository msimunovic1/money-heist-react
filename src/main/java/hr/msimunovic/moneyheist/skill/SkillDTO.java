package hr.msimunovic.moneyheist.skill;

import lombok.Data;

import java.util.List;

@Data
public class SkillDTO {

    private List<Skill> skills;
    private String mainSkill;

}
