package hr.msimunovic.moneyheist.skill.repository;

import hr.msimunovic.moneyheist.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Skill findByNameAndLevel(String name, String level);

}
