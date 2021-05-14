package hr.msimunovic.moneyheist.member_heist;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class MemberHeistId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "heist_id")
    private Long heistId;
}
