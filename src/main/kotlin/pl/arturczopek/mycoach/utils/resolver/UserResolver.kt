package pl.arturczopek.mycoach.utils.resolver

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebArgumentResolver
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pl.arturczopek.mycoach.model.database.User
import pl.arturczopek.mycoach.service.UserService
import pl.arturczopek.mycoach.service.UserStorage

/**
 * @Author Artur Czopek]
 * @Date 26-05-2017
 */
@Component
class UserResolver(
        val userService: UserService,
        val userStorage: UserStorage
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter?): Boolean {
        return parameter?.parameterType == User::class.java
    }

    override fun resolveArgument(parameter: MethodParameter?, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest?, binderFactory: WebDataBinderFactory?): Any {

        if (this.supportsParameter(parameter)) {
            if (userStorage.currentUser == null || userStorage.currentUser == User.emptyUser) {
                val token = webRequest?.getHeader("oauth_token")
                val user = userService.getUserByFbToken(token ?: "") ?: User.emptyUser
                userStorage.currentUser = user
            }

            return userStorage.currentUser!!
        } else {
            return WebArgumentResolver.UNRESOLVED
        }
    }
}
