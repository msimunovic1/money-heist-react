package hr.msimunovic.moneyheist.heist;

import hr.msimunovic.moneyheist.heist_skill.HeistSkill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Heist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "heistSeq")
    @SequenceGenerator(name = "heistSeq", sequenceName = "heist_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String location;

    private Timestamp startTime;

    private Timestamp endTime;

    @OneToMany(mappedBy = "heist",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<HeistSkill> skills = new HashSet<>();
}
