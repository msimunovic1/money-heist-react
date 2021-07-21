package hr.msimunovic.moneyheist.repository;

import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SkillRepositoryTest {

    @Autowired
    private SkillRepository skillRepository;

    @Test
    void testFindByName() {

        List<Skill> skills = skillRepository.findByName("combat");
        assertFalse(skills.isEmpty());

    }

    @Test
    void testFindByNameIgnoreCaseAndLevel() {

        Skill skill = skillRepository.findByNameIgnoreCaseAndLevel("COMbat", "*");
        assertNotNull(skill);
    }

    @Test
    void testFindByNameAndLevelIsGreaterThanEqual() {

        List<Skill> skill = skillRepository.findByNameAndLevelIsGreaterOrEqual("combat", 2);
        assertFalse(skill.isEmpty());
    }

}
