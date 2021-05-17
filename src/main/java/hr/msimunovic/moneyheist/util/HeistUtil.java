package hr.msimunovic.moneyheist.util;

import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;

import java.util.List;

public class HeistUtil {

    private HeistUtil() {
    }

    public static long calculateOutcomeInPercents(float requiredMembers, float participatedMembers) {
        return (long) ((participatedMembers / requiredMembers) * 100);
    }

    public static long determinateHeistRepercussion(List<MemberStatusEnum> memberStatuses) {

        // count members with EXPIRED status
        float expiredMembers = memberStatuses.stream()
                .filter(memberStatus -> memberStatus.equals(MemberStatusEnum.EXPIRED))
                .count();

        // count members with INCARCERATED status
        float incarceratedMembers = memberStatuses.stream()
                .filter(memberStatus -> memberStatus.equals(MemberStatusEnum.INCARCERATED))
                .count();

        return (long) (((expiredMembers+incarceratedMembers) / memberStatuses.size()) * 100);

    }

}

