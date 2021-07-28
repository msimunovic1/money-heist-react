package hr.msimunovic.moneyheist.heist_skill;

import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.skill.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HeistSkill {

    @EmbeddedId
    private HeistSkillId id = new HeistSkillId();

    @MapsId("heistId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Heist heist;

    @MapsId("skillId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Skill skill;

    private Integer members;
}
