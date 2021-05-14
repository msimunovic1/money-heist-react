package hr.msimunovic.moneyheist.member.service;

import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.MemberDTO;

public interface MemberService {
    Member saveMember(MemberDTO member);
}
