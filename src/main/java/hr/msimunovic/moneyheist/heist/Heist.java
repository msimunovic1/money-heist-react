package hr.msimunovic.moneyheist.heist;

import hr.msimunovic.moneyheist.common.enums.HeistOutcomeEnum;
import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import hr.msimunovic.moneyheist.heist_skill.HeistSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Heist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "heistSeq")
    @SequenceGenerator(name = "heistSeq", sequenceName = "heist_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String location;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private HeistStatusEnum status;

    @Enumerated(EnumType.STRING)
    private HeistOutcomeEnum outcome;

    @OneToMany(mappedBy = "heist",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<HeistSkill> skills = new HashSet<>();

    public void addSkill(Skill skill, Integer members) {

        HeistSkill heistSkill = new HeistSkill();
        heistSkill.setHeist(this);
        heistSkill.setSkill(skill);
        heistSkill.setMembers(members);

        skills.add(heistSkill);
        skill.getHeists().add(heistSkill);
    }
}
