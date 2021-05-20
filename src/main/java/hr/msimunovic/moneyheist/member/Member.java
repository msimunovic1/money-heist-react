package hr.msimunovic.moneyheist.member;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.heist_member.HeistMember;
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

    private String name;

    private String sex;

    private String email;

    @Enumerated(EnumType.STRING)
    private MemberStatusEnum status;

    @OneToMany(mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<MemberSkill> skills = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<HeistMember> heists = new HashSet<>();

    /*
     convention methods for data synchronization
     */
    public void addSkill(Skill skill, String mainSkill) {

        MemberSkill memberSkill = new MemberSkill();
        memberSkill.setMember(this);
        memberSkill.setSkill(skill);

        if(isMainSkill(skill.getName(), mainSkill)) {
            memberSkill.setMainSkill("Y");
        } else {
            memberSkill.setMainSkill("N");
        }

        skills.add(memberSkill);
        skill.getMembers().add(memberSkill);

    }

    public void addMemberSkill(MemberSkill memberSkill, String mainSkill) {

        if(isMainSkill(memberSkill.getSkill().getName(), mainSkill)) {
            memberSkill.setMainSkill("Y");
        } else {
            memberSkill.setMainSkill("N");
        }

        skills.add(memberSkill);
        memberSkill.setMember(this);
    }

    public void removeSkill(Skill skill) {

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

    public boolean isMainSkill(String skillName, String mainSkill) {

        if(mainSkill != null && skillName.equals(mainSkill)) {
            if (!skills.isEmpty()) {
                // reset old main skill
                resetOldMainSkill();
            }
            return true;
        } else {
            return false;
        }
    }

    public void resetOldMainSkill() {
        skills.stream()
                .filter(ms -> ms.getMainSkill().equals("Y"))
                .forEach(ms -> ms.setMainSkill("N"));
    }

}
