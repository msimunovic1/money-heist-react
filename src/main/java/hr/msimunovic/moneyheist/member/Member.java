package hr.msimunovic.moneyheist.member;

import com.sun.istack.NotNull;
import hr.msimunovic.moneyheist.common.MemberStatusEnum;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
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

    @NotNull
    private String name;

    @NotNull
    private String sex;

    @NotNull
    private String email;

    @NotNull
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

        if(mainSkill != null && skill.getName().equals(mainSkill)) {
            memberSkill.setMainSkill("Y");
        } else {
            memberSkill.setMainSkill("N");
        }

        skills.add(memberSkill);
        skill.getMembers().add(memberSkill);

    }

    public void removeSkill(String skillName) {

        Skill skill = new Skill();
        skill.setName(skillName);

        for (Iterator<MemberSkill> iterator = skills.iterator();
             iterator.hasNext(); ) {

            MemberSkill memberSkill = iterator.next();

            if(memberSkill.getMember().equals(this) && memberSkill.getSkill().equals(skill)) {
                iterator.remove();
                memberSkill.getSkill().getMembers().remove(memberSkill);
                memberSkill.setMember(null);
                memberSkill.setSkill(null);
            }
        }
    }


}
