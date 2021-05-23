package hr.msimunovic.moneyheist.heistMember;

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
public class HeistMember {

    @EmbeddedId
    private HeistMemberId id = new HeistMemberId();

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @MapsId("heistId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Heist heist;

}
