package pl.arturczopek.mycoach.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.arturczopek.mycoach.model.database.User
import pl.arturczopek.mycoach.service.UserService

/**
 * @Author Artur Czopek
 * @Date 19-05-2017
 */
@RestController
@RequestMapping("/user")
class UserController @Autowired constructor(val userService: UserService) {

    @Value("\${my-coach.client-address}")
    var clientAddress: String = ""

    @GetMapping("/")
    fun getUser(@RequestParam("oauth_token") token: String): User {
        var user: User? = userService.getFbUserByToken(token)

        if (user == null) {
            userService.createUser(token)
        }

        user = userService.getFbUserByToken(token)

        return user
    }

    @GetMapping("/create")
    fun create(@RequestParam("oauth_token") token: String): User {
        userService.createUser(token)
        return userService.getFbUserByToken(token)
    }


    @GetMapping("/clientUrl")
    fun getClientUrl(): String {
        return clientAddress
    }

    @GetMapping("/token")
    fun getToken(): ResponseEntity<String> {
        val principal = SecurityContextHolder.getContext().authentication

        try {
            return ResponseEntity.ok(
                    (principal.details as OAuth2AuthenticationDetails).tokenValue)

        } catch (ex: Exception) {
            // no user or anonymous user, return forbidden status
            return ResponseEntity.ok("")
        }
    }
}