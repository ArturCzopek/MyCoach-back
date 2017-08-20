package pl.arturczopek.mycoach.utils.resolver

import mu.KLogging
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebArgumentResolver
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
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

        val token = webRequest?.getHeader("oauth-token")
        logger.info { "Token: $token" }

        val sra = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val req = sra.request

        logger.info("token 1req, ${req.getHeader("oauth-token")}")

        logger.info { "Headers" }

        for (header in req.headerNames) {
            logger.info { header }
        }

        if (this.supportsParameter(parameter) && token != null) {
            logger.info("Supports")
            if (userStorage.getUserByToken(token) == User.emptyUser) {
                logger.info("Not user in map")
                logger.info("map")
                for ((k, v) in userStorage.getUsers()) {
                    logger.info("$k, $v")
                }

                logger.info("get him by fb token")
                val user = userService.getUserByFbToken(token) ?: User.emptyUser
                logger.info("user: $user")
                userStorage.addUser(token, user)
                logger.info("Is added? ")
                for ((k, v) in userStorage.getUsers()) {
                    logger.info("$k, $v")
                }
            }

            logger.info("Not, it's time to get him by token: ${userStorage.getUserByToken(token)}")
            return userStorage.getUserByToken(token)
        } else {
            return WebArgumentResolver.UNRESOLVED
        }
    }

    companion object : KLogging()
}
