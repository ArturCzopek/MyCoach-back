package pl.arturczopek.mycoach.config;

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Configuration
@ComponentScan(basePackages = arrayOf("pl.arturczopek.mycoach"))
//@EnableWebSecurity
open class SecurityConfiguration {

    @Value("\${my-coach.client-address}")
    var clientAddress: String = ""

    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

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
        val bean: FilterRegistrationBean = FilterRegistrationBean(CorsFilter(source))
        bean.order = 0
        return bean
    }

    companion object : KLogging()
}