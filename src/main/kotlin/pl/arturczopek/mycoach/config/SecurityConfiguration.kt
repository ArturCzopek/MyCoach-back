package pl.arturczopek.mycoach.config;

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import pl.arturczopek.mycoach.service.LogoutUserHandler
import pl.arturczopek.mycoach.service.UserStorage


/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Value("\${my-coach.client-address}")
    lateinit var clientAddress: String

    @Value("\${my-coach.redirect-address}")
    lateinit var redirectAddress: String

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    @Autowired
    lateinit var userStorage: UserStorage

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
//                .antMatchers("/", "/index.html", "/login**", "/user/**", "/db-console/**", "/dictionary/**").permitAll()
                .antMatchers("/**").permitAll()
                .antMatchers("/admin/**").hasRole("Admin")
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/"))
                .and().logout().logoutUrl("/logout").addLogoutHandler(LogoutUserHandler(userStorage, "$clientAddress/#/logout"))
                .and().csrf().disable().headers().frameOptions().disable()
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurerAdapter() {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                clientAddress,
                                clientAddress.replace("www.", ""),
                                redirectAddress,
                                redirectAddress.replace("www.", "")
                        )
                        .allowedHeaders("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .maxAge(3600)

                logger.info { "Allowed origins: $clientAddress, $redirectAddress" }
            }
        }
    }

    companion object : KLogging()
}