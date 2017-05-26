package pl.arturczopek.mycoach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "pl.arturczopek.mycoach.model.database")
@EnableCaching
@SpringBootApplication
@EnableJpaRepositories
public class MyCoachInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MyCoachInitializer.class, args);
    }
}
