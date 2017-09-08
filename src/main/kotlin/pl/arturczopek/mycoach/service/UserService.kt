package pl.arturczopek.mycoach.service

import mu.KLogging
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pl.arturczopek.mycoach.exception.NotFoundUserException
import pl.arturczopek.mycoach.model.database.User
import pl.arturczopek.mycoach.model.database.UserSettings
import pl.arturczopek.mycoach.repository.LanguageRepository
import pl.arturczopek.mycoach.repository.RoleRepository
import pl.arturczopek.mycoach.repository.UserRepository
import pl.arturczopek.mycoach.repository.UserSettingRepository
import java.net.URI

/**
 * @Author Artur Czopek
 * @Date 19-05-2017
 */

typealias FbData = HashMap<String, Any>

@Service
class UserService(
        private val restTemplate: RestTemplate,
        private val userRepository: UserRepository,
        private val userSettingsRepository: UserSettingRepository,
        private val roleRepository: RoleRepository,
        private val languageRepository: LanguageRepository,
        private val userStorage: UserStorage
) {

    fun getUserByFbToken(token: String): User? {

        if (token.isNullOrBlank()) return User.emptyUser

        val userMap: FbData = getUserFbData(token)
        this.userStorage.addUserFbData(token, userMap)
        return userRepository.findOneByFbId(userMap["id"] as String)
    }

    fun getToken(): String {
        val principal = SecurityContextHolder.getContext().authentication

        return try {
            (principal.details as OAuth2AuthenticationDetails).tokenValue
        } catch (e: Exception) {
            logger.warn("Problem with fetching token: ${e.message}")
            ""
        }
    }

    fun createUser(token: String?) {

        if (token.isNullOrBlank()) return

        val userMap = getUserFbData(token!!)

        if (userMap["id"] == null || userMap["name"] == null) {
            logger.warn("Not found FbData for token $token")
            return
        }

        val settings = getInitSettingsAndSave(userMap["id"] as String, token)
        val userRole = roleRepository.findOneByRoleName("user")

        val user = User().apply {
            fbId = userMap["id"] as String
            name = userMap["name"] as String
            userSettings = settings
            role = userRole
        }

        userRepository.save(user)

    }

    // ADMIN FUNCTIONS

    fun getAllUsers(): MutableIterable<User> = userRepository.findAll()

    fun toggleActiveUserStatus(userId: Long) {
        val user = this.userRepository.findOne(userId)
        user.active = !user.active
        userRepository.save(user)
    }

    fun toggleUserRole(userId: Long) {
        val user = this.userRepository.findOne(userId)

        when (user.role.roleName) {
            "User" -> user.role = roleRepository.findOneByRoleName("Admin")
            "Admin" -> user.role = roleRepository.findOneByRoleName("User")
            else -> logger.error { "User $userId has not role 'User' or 'Admin' but ${user.role.roleName}" }
        }

        userRepository.save(user)
    }

    private fun getUserFbData(token: String): FbData {

        try {
            userStorage.getUserFbDataByToken(token)
        } catch (e: NotFoundUserException) {
            token?.let {
                createRequest("https://graph.facebook.com/me?access_token=$token")
                        .run { this.body}
                        .run { userStorage.addUserFbData(token, this) }
            }
        } finally {
            return userStorage.getUserFbDataByToken(token)
        }
    }

    private fun getInitSettingsAndSave(userId: String, token: String): UserSettings {
        val userMap: FbData = createRequest("https://graph.facebook.com/v2.9/$userId?fields=email&access_token=$token").run { this.body }

        val userSettings = UserSettings().apply {
            infoMail = userMap["email"] as String
            language = languageRepository.findOne(1) // POLISH FOR NOW
        }

        userSettingsRepository.save(userSettings)
        return userSettings
    }

    private fun createRequest(url: String) = URI.create(url)
                .run { RequestEntity<Any>(HttpMethod.GET, this) }
                .run { restTemplate.exchange(this, object : ParameterizedTypeReference<FbData>() {}) }

    companion object : KLogging()
}