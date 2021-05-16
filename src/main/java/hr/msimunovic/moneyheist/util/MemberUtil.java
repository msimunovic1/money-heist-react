package hr.msimunovic.moneyheist.util;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;

import java.util.List;
import java.util.Random;

public class MemberUtil {

/*    private static final List<MemberStatusEnum> STATUSES =  List.of(MemberStatusEnum.values());
           // List.of(MemberStatusEnum.EXPIRED, MemberStatusEnum.INCARCERATED);
    private static final int SIZE = STATUSES.size();*/
    private static final Random RANDOM = new Random();

    public static MemberStatusEnum determineMemberStatus(List<MemberStatusEnum> memberStatuses)  {
        return memberStatuses.get(RANDOM.nextInt(memberStatuses.size()));
    }
}
