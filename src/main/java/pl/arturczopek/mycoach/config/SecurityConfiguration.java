package pl.arturczopek.mycoach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Configuration
@ComponentScan(basePackages = { "pl.arturczopek.mycoach" })
//@EnableWebSecurity
public class SecurityConfiguration{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
