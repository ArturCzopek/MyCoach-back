package pl.arturczopek.mycoach;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@EntityScan(basePackages = "pl.arturczopek.mycoach.database.entity")
@SpringBootApplication
@EnableJpaRepositories
public class MyCoachInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MyCoachInitializer.class, args);
	}
}
