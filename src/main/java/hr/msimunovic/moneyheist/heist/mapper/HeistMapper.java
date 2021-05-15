package hr.msimunovic.moneyheist.heist.mapper;

import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.HeistDTO;
import hr.msimunovic.moneyheist.heist.dto.HeistSkillDTO;
import hr.msimunovic.moneyheist.heist_skill.HeistSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class HeistMapper {

    private final SkillService skillService;
    private final ModelMapper modelMapper;

    public HeistDTO mapHeistToDTO(Heist heist) {

        HeistDTO heistDTO = new HeistDTO();
        heistDTO.setName(heist.getName());
        heistDTO.setLocation(heist.getLocation());
        heistDTO.setStartTime(heist.getStartTime());
        heistDTO.setEndTime(heist.getEndTime());

        List<HeistSkillDTO> heistSkillDTOList = heist.getSkills().stream()
                .map(heistSkill -> mapHeistSkillToDTO(heistSkill))
                .collect(Collectors.toList());

        heistDTO.setSkills(heistSkillDTOList);

        return heistDTO;

    }

    public Heist mapDTOToHeist(HeistDTO heistDTO) {

        Heist heist = new Heist();
        heist.setName(heistDTO.getName());
        heist.setLocation(heistDTO.getLocation());
        heist.setStartTime(heistDTO.getStartTime());
        heist.setEndTime(heistDTO.getEndTime());

        heistDTO.getSkills()
                .forEach(skill -> {

                    Skill skillFromDB = skillService.checkSkillInDB(skill.getName(), skill.getLevel());

                    if(skillFromDB != null) {
                        heist.addSkill(skillFromDB, skill.getMembers());
                    } else {
                        heist.addSkill(modelMapper.map(skill, Skill.class), skill.getMembers());
                    }
                });

        return heist;
    }

    public List<HeistSkillDTO> mapHeistSkillsToDTO(Set<HeistSkill> heistSkills) {

        return heistSkills.stream()
                .map(heistSkill -> mapHeistSkillToDTO(heistSkill))
                .collect(Collectors.toList());
    }

    public HeistSkillDTO mapHeistSkillToDTO(HeistSkill heistSkill) {

        HeistSkillDTO heistSkillDTO = new HeistSkillDTO();
        heistSkillDTO.setName(heistSkill.getSkill().getName());
        heistSkillDTO.setLevel(heistSkill.getSkill().getLevel());
        heistSkillDTO.setMembers(heistSkill.getMembers());

        return heistSkillDTO;
    }
}
