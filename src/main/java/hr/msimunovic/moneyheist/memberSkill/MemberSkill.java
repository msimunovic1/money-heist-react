package hr.msimunovic.moneyheist.memberSkill;

import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.skill.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MemberSkill {

    @EmbeddedId
    private MemberSkillId id = new MemberSkillId();

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @MapsId("skillId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Skill skill;

    private String mainSkill;

}
