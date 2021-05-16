package hr.msimunovic.moneyheist.util;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;

import java.util.List;
import java.util.Random;

public class MemberUtil {

    private MemberUtil() {
    }

    private static final Random RANDOM = new Random();

    public static MemberStatusEnum determineMemberStatus(List<MemberStatusEnum> memberStatuses)  {
        return memberStatuses.get(RANDOM.nextInt(memberStatuses.size()));
    }
}
