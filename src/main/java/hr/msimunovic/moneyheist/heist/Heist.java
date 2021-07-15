package hr.msimunovic.moneyheist.heist;

import hr.msimunovic.moneyheist.common.enums.HeistOutcomeEnum;
import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import hr.msimunovic.moneyheist.heistMember.HeistMember;
import hr.msimunovic.moneyheist.heistSkill.HeistSkill;
import hr.msimunovic.moneyheist.member.Member;
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

    @OneToMany(mappedBy = "heist",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<HeistMember> members = new HashSet<>();

    /*
     convention methods for data synchronization
     */
    public void addSkill(Skill skill, Integer members) {

        var existedHeistSkill = findExistedHeistSkill(skill.getHeists());

        var heistSkill = new HeistSkill();

        if(existedHeistSkill==null) {
            heistSkill.setHeist(this);
            heistSkill.setSkill(skill);
        } else {
            heistSkill = existedHeistSkill;
        }

        heistSkill.setMembers(members);
        skills.add(heistSkill);
        skill.getHeists().add(heistSkill);
    }

    public HeistSkill findExistedHeistSkill(Set<HeistSkill> heistSkills) {
        return heistSkills.stream()
                .filter(heistSkill -> heistSkill.getHeist().getId().equals(this.getId()))
                .findAny()
                .orElse(null);
    }

    public void addMember(Member member) {

        var heistMember = new HeistMember();
        heistMember.setHeist(this);
        heistMember.setMember(member);

        members.add(heistMember);
        member.getHeists().add(heistMember);
    }
}
