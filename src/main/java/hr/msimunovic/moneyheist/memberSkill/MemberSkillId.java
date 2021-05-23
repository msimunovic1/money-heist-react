package hr.msimunovic.moneyheist.memberSkill;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class MemberSkillId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "skill_id")
    private Long skillId;

}
