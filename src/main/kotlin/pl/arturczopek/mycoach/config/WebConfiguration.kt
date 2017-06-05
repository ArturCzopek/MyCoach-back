package pl.arturczopek.mycoach.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import pl.arturczopek.mycoach.utils.resolver.UserResolver


//@EnableWebMvc
@Configuration
open class WebConfiguration(val userResolver: UserResolver) : WebMvcConfigurerAdapter() {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>?) {
        argumentResolvers?.add(userResolver)
    }

    override fun addViewControllers(registry: ViewControllerRegistry?) {
        registry?.run {
            addViewController("/").setViewName("home")
            addViewController("/login").setViewName("login")
            addViewController("/logout").setViewName("logout")
        }
    }
}