package hr.msimunovic.moneyheist.heist.repository;

import hr.msimunovic.moneyheist.heist.Heist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeistRepository extends JpaRepository<Heist, Long> {

    Heist findByName(String name);
}
