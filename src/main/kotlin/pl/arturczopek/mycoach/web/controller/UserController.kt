package pl.arturczopek.mycoach.web.controller

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.arturczopek.mycoach.exception.InactiveUserException
import pl.arturczopek.mycoach.model.database.User
import pl.arturczopek.mycoach.service.DictionaryService
import pl.arturczopek.mycoach.service.UserService
import pl.arturczopek.mycoach.service.UserStorage

/**
 * @Author Artur Czopek
 * @Date 19-05-2017
 */
@RestController
@RequestMapping("/user")
class UserController(
        val userService: UserService,
        val userStorage: UserStorage,
        val dictionaryService: DictionaryService
) {

    @Value("\${my-coach.client-address}")
    var clientAddress: String = ""

    @Value("\${my-coach.redirect-address}")
    var redirectAddress: String = ""

    @GetMapping(value = *arrayOf("/", ""))
    fun getUser(@RequestHeader(value = "oauth-token", required = false) token: String?): User? {

        if (token.isNullOrBlank()) return null

        var user: User? = userService.getUserByFbToken(token!!)

        if (user == null) {
            userService.createUser(token)
        }

        user = userService.getUserByFbToken(token)

        if (!user!!.active) {
            throw InactiveUserException(dictionaryService.translate("global.error.inactiveUser.message", userStorage.getUserByToken(token).userId).value)
        }

        userStorage.addUser(token, user)

        return user
    }

    @GetMapping("/clientUrl")
    fun getClientUrl(): String = clientAddress

    @GetMapping("/redirectUrl")
    fun getRedirectUrl(): String = redirectAddress

    @GetMapping("/token")
    fun getToken(): ResponseEntity<String> {
        val token = userService.getToken()

        return if (token.isNullOrBlank()) ResponseEntity.status(401).body("Cannot find token") else ResponseEntity.ok(token)
    }

    companion object: KLogging()
}
