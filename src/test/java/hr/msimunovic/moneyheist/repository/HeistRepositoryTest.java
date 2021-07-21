package hr.msimunovic.moneyheist.repository;

import hr.msimunovic.moneyheist.common.enums.HeistOutcomeEnum;
import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.repository.HeistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HeistRepositoryTest {

    @Autowired
    private HeistRepository heistRepository;

    @Test
    void testFindAll() {

        List<Heist> heists = heistRepository.findAll();
        assertFalse(heists.isEmpty());
    }

    @Test
    void testFindById() {

        Optional<Heist> heist = heistRepository.findById(100L);
        assertTrue(heist.isPresent());
    }

    @Test
    void testFindByName() {

        Heist heist = heistRepository.findByName("European Central Bank - ECB");
        assertNotNull(heist);
    }

    @Test
    void testFindByNameIgnoreCase() {

        Heist heist = heistRepository.findByNameIgnoreCase("EUROPEAN central BANK - ecb");
        assertNotNull(heist);
    }

    @Test
    void testFindByStatusOrStatus() {

        List<Heist> heists = heistRepository.findByStatusOrStatus(HeistStatusEnum.FINISHED, HeistStatusEnum.READY);
        assertFalse(heists.isEmpty());
    }

    @Test
    void testSave() {

        Heist heist = new Heist();
        heist.setId(1L);
        heist.setName("HNB");
        heist.setLocation("Zagreb");
        heist.setStartTime(LocalDateTime.now());
        heist.setEndTime(LocalDateTime.now().plusDays(3));
        heist.setStatus(HeistStatusEnum.READY);
        heist.setOutcome(HeistOutcomeEnum.SUCCEEDED);

        Heist savedHeist = heistRepository.save(heist);

        assertNotNull(savedHeist);
    }
}
