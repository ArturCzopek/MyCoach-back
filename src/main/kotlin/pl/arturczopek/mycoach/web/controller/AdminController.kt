package pl.arturczopek.mycoach.web.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.arturczopek.mycoach.exception.WrongPermissionException
import pl.arturczopek.mycoach.model.database.User
import pl.arturczopek.mycoach.service.DictionaryService
import pl.arturczopek.mycoach.service.UserService
import pl.arturczopek.mycoach.service.UserStorage

/**
 * @Author arturczopek
 * @Date 29-06-2017
 */
@RestController
@RequestMapping("/admin")
class AdminController(
        val userService: UserService,
        val userStorage: UserStorage,
        val dictionaryService: DictionaryService
) {

    @GetMapping("/users")
    fun getAllUsers(): MutableIterable<User> {
        if (!userService.isLoggedInUserAdmin()) {
            throw WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userStorage.currentUser!!.userId).value)
        }

        return userService.getAllUsers()
    }

    @PostMapping("/toggleActive")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Changed active status")
    fun toggleActiveUserStatus(@RequestParam userId: Long) {
        if (!userService.isLoggedInUserAdmin()) {
            throw WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userStorage.currentUser!!.userId).value)
        }

        userService.toggleActiveUserStatus(userId)
    }
}