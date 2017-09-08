package pl.arturczopek.mycoach.config

import com.google.common.base.Predicates.and
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * @Author Artur Czopek
 * @Date 3-10-2016
 */

@Profile("dev")
@Configuration
@EnableSwagger2
@ConfigurationProperties(prefix = "my-coach.swagger")
class SwaggerConfiguration {

    var title: String = "My Coach"
    var version: String = "1.2.0"
    var description: String = "My Coach"
    var contactName: String = "Artur Czopek"
    var contactAddress: String = "artur@simplecoding.pl"
    var contactUrl: String = "http://simplecoding.pl"

    private val NO_JSON_REGEX = "^(?!.*json).*$"
    private val NO_ERROR_REGEX = "(?!.*error).*$"

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(paths())
            .build();

    private fun apiInfo() = ApiInfoBuilder()
            .title(title)
            .description(description)
            .version(version)
            .contact(Contact(contactName, contactUrl, contactAddress))
            .build()

    private fun paths() = and(
            regex(NO_JSON_REGEX),
            regex(NO_ERROR_REGEX)
    )
}
