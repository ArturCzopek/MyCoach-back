package pl.arturczopek.mycoach.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Configuration
@ComponentScan(basePackages = { "pl.arturczopek.mycoach" })
@Slf4j
//@EnableWebSecurity
public class SecurityConfiguration{

    @Value("${my-coach.client-address}")
    private String clientAddress;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        log.info("Allowed client: {}", clientAddress);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setMaxAge(3600l);
        config.setAllowCredentials(true);
        config.addAllowedOrigin(clientAddress);
        config.addAllowedOrigin(clientAddress.replace("www.", ""));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}