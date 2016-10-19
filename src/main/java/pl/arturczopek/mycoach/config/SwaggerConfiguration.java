package pl.arturczopek.mycoach.config;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.and;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @Author Artur Czopek
 * @Date 3-10-2016
 */

@Profile("dev")
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${myCoach.doc-title}")
    private String title;

    @Value("${myCoach.version}")
    private String version;

    @Value("${myCoach.doc-description}")
    private String description;

    @Value("${myCoach.contact-name}")
    private String contactName;

    @Value("${myCoach.contact-address}")
    private String contactAddress;

    @Value("${myCoach.contact-url}")
    private String contactUrl;

    private static final String NO_JSON_REGEX = "^(?!.*json).*$";
    private static final String NO_ERROR_REGEX = "(?!.*error).*$";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(paths())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .contact(new Contact(contactName, contactUrl, contactAddress))
                .build();
    }

    private Predicate<String> paths() {
        return and(
                regex(NO_JSON_REGEX),
                regex(NO_ERROR_REGEX)
        );
    }
}
