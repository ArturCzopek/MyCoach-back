package pl.arturczopek.mycoach.web.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.arturczopek.mycoach.model.database.User
import pl.arturczopek.mycoach.service.UserService
import pl.arturczopek.mycoach.service.UserStorage

/**
 * @Author Artur Czopek
 * @Date 19-05-2017
 */
@RestController
@RequestMapping("/user")
open class UserController(
        val userService: UserService,
        val userStorage: UserStorage
) {

    @Value("\${my-coach.client-address}")
    var clientAddress: String = ""

    @Value("\${my-coach.redirect-address}")
    var redirectAddress: String = ""

    @GetMapping("/")
    fun getUser(@RequestHeader("oauth_token") token: String): User? {
        var user: User? = userService.getUserByFbToken(token)

        if (user == null) {
            userService.createUser(token)
        }

        user = userService.getUserByFbToken(token)
        userStorage.currentUser = user

        return user
    }

    @GetMapping("/clientUrl")
    fun getClientUrl(): String = clientAddress

    @GetMapping("/redirectUrl")
    fun getRedirectUrl(): String = redirectAddress

    @GetMapping("/token")
    fun getToken(): ResponseEntity<String> {
        val principal = SecurityContextHolder.getContext().authentication

        try {
            return ResponseEntity.ok(
                    (principal.details as OAuth2AuthenticationDetails).tokenValue)

        } catch (ex: Exception) {
            // no user or anonymous user, return empty data
            return ResponseEntity.ok("")
        }
    }
}