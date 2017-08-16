package pl.arturczopek.mycoach.service

import mu.KLogging
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * @Author Artur Czopek
 * @Date 10-08-2017
 */
open class LogoutUserHandler(
        val userStorage: UserStorage,
        val logoutUrl: String
) : SimpleUrlLogoutSuccessHandler(), LogoutHandler {

    @Throws(IOException::class, ServletException::class)
    override fun logout(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authentication: Authentication?
    ) {

        var token: String = "MISSING_TOKEN"

        try {
            response?.sendRedirect(logoutUrl)
            token = (authentication?.details as OAuth2AuthenticationDetails).tokenValue ?: "MISSING_TOKEN"
            SecurityContextLogoutHandler().logout(request, response, authentication)
            userStorage.clearUser(token)
            logger.info("Cleared user with token $token")
            super.onLogoutSuccess(request, response, authentication)
        } catch (ex: Exception) {
            logger.info("Cannot clear user with token $token, it doesn't exist")
        }
    }

    companion object : KLogging()
}