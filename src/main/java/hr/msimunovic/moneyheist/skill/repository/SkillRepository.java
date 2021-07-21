package hr.msimunovic.moneyheist.skill.repository;

import hr.msimunovic.moneyheist.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Skill findByNameIgnoreCaseAndLevel(String name, String level);

    List<Skill> findByName(String name);

    @Query(value = "SELECT * FROM Skill s WHERE s.name = :name and LENGTH(s.level) >= :levelLength",
            nativeQuery = true)
    List<Skill> findByNameAndLevelIsGreaterOrEqual(String name, Integer levelLength);

}
