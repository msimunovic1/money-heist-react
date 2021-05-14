package hr.msimunovic.moneyheist.skill.service;

import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public Skill checkSkillInDB(String name, String level) {

        return skillRepository.findByNameAndLevel(name, level);
    }

}
