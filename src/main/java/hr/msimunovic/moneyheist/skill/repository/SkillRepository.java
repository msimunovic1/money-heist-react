package hr.msimunovic.moneyheist.skill.repository;

import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    //Skill findByName(String name);


}
