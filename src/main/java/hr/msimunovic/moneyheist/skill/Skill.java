package hr.msimunovic.moneyheist.skill;

import hr.msimunovic.moneyheist.heist_skill.HeistSkill;
import hr.msimunovic.moneyheist.member_skill.MemberSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skillSeq")
    @SequenceGenerator(name = "skillSeq", sequenceName = "skill_seq", allocationSize = 1)
    private Long id;

    @NotEmpty(message = "Heist name field is required")
    @ColumnTransformer(write = "LOWER(?)")
    private String name;

    private String level;

    @OneToMany(mappedBy = "skill")
    private Set<MemberSkill> members = new HashSet<>();

    @OneToMany(mappedBy = "skill")
    private Set<HeistSkill> heists = new HashSet<>();

}
