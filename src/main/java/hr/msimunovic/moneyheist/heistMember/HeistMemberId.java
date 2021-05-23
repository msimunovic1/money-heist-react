package hr.msimunovic.moneyheist.heistMember;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class HeistMemberId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "heist_id")
    private Long heistId;
}
