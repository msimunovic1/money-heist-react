package hr.msimunovic.moneyheist.repository;

import hr.msimunovic.moneyheist.common.enums.MemberSexEnum;
import hr.msimunovic.moneyheist.common.enums.MemberStatusEnum;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testFindAll() {

        List<Member> members = memberRepository.findAll();
        assertFalse(members.isEmpty());
    }

    @Test
    void testFindById() {

        Optional<Member> member = memberRepository.findById(100L);
        assertTrue(member.isPresent());
    }

    @Test
    void testFindByName() {

        Optional<Member> member = memberRepository.findByName("Tokyo");
        assertTrue(member.isPresent());
    }

    @Test
    void testFindByEmail() {

        Member member = memberRepository.findByEmail("tokyo@ag04.com");
        assertNotNull(member);
    }

    @Test
    void testSave() {

        Member member = new Member();
        member.setId(1L);
        member.setName("Tokyo");
        member.setSex(MemberSexEnum.F);
        member.setEmail("tokyo@ag04.com");
        member.setStatus(MemberStatusEnum.AVAILABLE);

        Member savedMember = memberRepository.save(member);

        assertNotNull(savedMember);
    }
}
