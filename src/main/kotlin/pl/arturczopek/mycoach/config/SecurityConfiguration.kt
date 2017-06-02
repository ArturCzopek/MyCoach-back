package pl.arturczopek.mycoach.config;

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.web.client.RestTemplate
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import javax.servlet.Filter

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Configuration
//@EnableWebSecurity
@EnableOAuth2Sso
open class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Value("\${my-coach.client-address}")
    lateinit var clientAddress: String

    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    open fun restTemplate(): RestTemplate = RestTemplate()

    override fun configure(http: HttpSecurity) {
        http
                .antMatcher("/**").authorizeRequests()
//                .antMatchers("/", "/index.html", "/login**", "/user/**", "/db-console/**", "/dictionary/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/"))
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("$clientAddress/logout")
                .invalidateHttpSession(true).deleteCookies("oauth_token")
                .and().csrf().disable().headers().frameOptions().disable()
    }

    @Bean
    open fun corsFilter(): FilterRegistrationBean {
        logger.info("Allowed client: $clientAddress")
        val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.run {
            maxAge = 3600L
            allowCredentials = true
            addAllowedOrigin(clientAddress)
            addAllowedOrigin(clientAddress.replace("www.", ""))
            addAllowedHeader("*")
            addAllowedMethod("*")
        }
        source.registerCorsConfiguration("/**", config)
        val bean: FilterRegistrationBean = FilterRegistrationBean(CorsFilter(source) as Filter)
        bean.order = 0
        return bean
    }

    companion object : KLogging()
}