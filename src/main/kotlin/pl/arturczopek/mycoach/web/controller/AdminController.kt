package pl.arturczopek.mycoach.web.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.arturczopek.mycoach.service.AppDataService
import pl.arturczopek.mycoach.service.DictionaryService
import pl.arturczopek.mycoach.service.UserService

/**
 * @Author Artur Czopek
 * @Date 29-06-2017
 */
@RestController
@RequestMapping("/admin")
class AdminController(
        val userService: UserService,
        val dictionaryService: DictionaryService,
        val appDataService: AppDataService
) {

    @GetMapping("/users")
    fun getAllUsers() = userService.getAllUsers()

    @GetMapping("/appData")
    fun getAppData() = appDataService.getAppData()

    @PostMapping("/toggleActive")
    @ResponseStatus(value = HttpStatus.OK, reason = "Changed active status")
    fun toggleActiveUserStatus(@RequestParam userId: Long) = userService.toggleActiveUserStatus(userId)

    @PostMapping("/toggleRole")
    @ResponseStatus(value = HttpStatus.OK, reason = "Changed active status")
    fun toggleUserRole(@RequestParam userId: Long) = userService.toggleUserRole(userId)
}