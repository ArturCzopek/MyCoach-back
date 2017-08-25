package pl.arturczopek.mycoach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@EntityScan(basePackages = "pl.arturczopek.mycoach.model.database")
@EnableCaching
@SpringBootApplication
public class MyCoachInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MyCoachInitializer.class, args);
    }
}
