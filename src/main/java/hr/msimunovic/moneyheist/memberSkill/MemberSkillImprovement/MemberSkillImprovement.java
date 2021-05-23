package hr.msimunovic.moneyheist.memberSkill.MemberSkillImprovement;

import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heistMember.HeistMember;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import hr.msimunovic.moneyheist.memberSkill.MemberSkill;
import hr.msimunovic.moneyheist.skill.Skill;
import hr.msimunovic.moneyheist.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class MemberSkillImprovement {

    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseMemberSkills(Heist heist) {

        log.info("Member skill for heist with id {} improvement started.", heist.getId());

        for (HeistMember heistMember : heist.getMembers()) {

            Member member = memberRepository.findById(heistMember.getMember().getId())
                    .orElseThrow(() -> new NotFoundException(Constants.MSG_MEMBER_NOT_FOUND));

            List<MemberSkill> memberSkills = new ArrayList<>(member.getSkills());
            for (MemberSkill memberSkill : memberSkills) {
                if(memberSkill.getSkill().getLevel().length() < Constants.MAX_SKILL_LEVEL) {

                    String mainSkill = null;
                    if(memberSkill.getMainSkill().equals("Y")) {
                        mainSkill = memberSkill.getSkill().getName();
                    }

                    StringBuilder stringBuilder = new StringBuilder(memberSkill.getSkill().getLevel());
                    String increasedLevel = stringBuilder.append("*").toString();

                    Skill skillFromDB = skillRepository.findByNameAndLevel(memberSkill.getSkill().getName(), increasedLevel);
                    if(skillFromDB==null) {
                        Skill skill = new Skill();
                        skill.setName(memberSkill.getSkill().getName());
                        skill.setLevel(increasedLevel);
                        member.addSkill(skill, mainSkill);
                    } else {
                        member.addSkill(skillFromDB, mainSkill);
                    }
                }
            }
        }
    }

}
