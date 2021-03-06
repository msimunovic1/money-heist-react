package hr.msimunovic.moneyheist.heist_member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HeistMemberId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "heist_id")
    private Long heistId;
}
