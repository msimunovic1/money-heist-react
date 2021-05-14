package hr.msimunovic.moneyheist.member_heist;

import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MemberHeist {

    @EmbeddedId
    private MemberHeistId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @MapsId("heistId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Heist heist;

}
