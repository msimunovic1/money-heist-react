package hr.msimunovic.moneyheist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MoneyHeistApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyHeistApplication.class, args);
	}

}
