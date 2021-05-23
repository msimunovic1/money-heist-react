package hr.msimunovic.moneyheist.heistSkill;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class HeistSkillId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "heist_id")
    private Long heistId;

    @Column(name = "skill_id")
    private Long skillId;
}
