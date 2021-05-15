package hr.msimunovic.moneyheist.member.repository;

import hr.msimunovic.moneyheist.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = { "skills.skill" })
    Optional<Member> findById(Long aLong);
}
