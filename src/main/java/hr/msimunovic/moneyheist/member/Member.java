package hr.msimunovic.moneyheist.member;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.heist_member.HeistMember;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addSkill(Skill skill, String mainSkill) {

        // check does skill with same name exists
        // if Member Skill with same name and different level exits remove it from relationship
        skills.removeIf(memberSkill -> memberSkill.getSkill().getName().equals(skill.getName()) &&
                !memberSkill.getSkill().getLevel().equals(skill.getLevel()));

        MemberSkill existedMemberSkill = findExistedMemberSkill(skill.getMembers());

        MemberSkill memberSkill = new MemberSkill();

        if(existedMemberSkill==null) {
            memberSkill.setMember(this);
            memberSkill.setSkill(skill);
        } else {
            memberSkill = existedMemberSkill;
        }

        skills.add(memberSkill);
        skill.getMembers().add(memberSkill);

        updateMainSkill(mainSkill);

    }

    public MemberSkill findExistedMemberSkill(Set<MemberSkill> memberSkills) {
        return memberSkills.stream()
                .findAny()
                .orElse(null);
    }

    public void removeSkills(Skill skill) {

        for (Iterator<MemberSkill> iterator = skills.iterator(); iterator.hasNext(); ) {

            MemberSkill memberSkill = iterator.next();

            if(memberSkill.getMember().equals(this) && memberSkill.getSkill().equals(skill)) {
                iterator.remove();
                memberSkill.getSkill().getMembers().remove(memberSkill);
                memberSkill.setMember(null);
                memberSkill.setSkill(null);
            }
        }
    }
    
    public void updateMainSkill(String mainSkill) {
        skills.forEach(memberSkill -> {
                if(memberSkill.getSkill().getName().equals(mainSkill)) {
                    memberSkill.setMainSkill("Y");
                } else {
                    memberSkill.setMainSkill("N");
                }
            });
    }

}
