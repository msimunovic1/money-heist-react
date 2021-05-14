package hr.msimunovic.moneyheist.skill.service;

import hr.msimunovic.moneyheist.skill.Skill;

public interface SkillService {

    Skill checkSkillInDB(String name, String level);
}
