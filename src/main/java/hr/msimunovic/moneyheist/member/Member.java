package hr.msimunovic.moneyheist.member;

import hr.msimunovic.moneyheist.common.MemberStatusEnum;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
import hr.msimunovic.moneyheist.member_skill.MemberSkillId;
import hr.msimunovic.moneyheist.skill.Skill;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memberSeq")
    @SequenceGenerator(name = "memberSeq", sequenceName = "member_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String sex;

    private String email;

    @Enumerated(EnumType.STRING)
    private MemberStatusEnum status;

    @OneToMany(mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<MemberSkill> skills = new HashSet<>();

    public void addSkill(Skill skill, String mainSkill) {

        MemberSkill memberSkill = new MemberSkill();
        memberSkill.setMember(this);
        memberSkill.setSkill(skill);

        if(skill.getName().equals(mainSkill)) {
            memberSkill.setMainSkill("Y");
        } else {
            memberSkill.setMainSkill("N");
        }

        skills.add(memberSkill);
        skill.getMembers().add(memberSkill);

    }
}
