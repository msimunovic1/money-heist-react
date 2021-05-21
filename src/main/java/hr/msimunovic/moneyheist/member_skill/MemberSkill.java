package hr.msimunovic.moneyheist.member_skill;

import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.skill.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Immutable
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
