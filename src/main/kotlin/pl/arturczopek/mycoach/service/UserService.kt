package pl.arturczopek.mycoach.service

import mu.KLogging
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
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
open class UserService(
        val restTemplate: RestTemplate,
        val userRepository: UserRepository,
        val userSettingRepository: UserSettingRepository,
        val roleRepository: RoleRepository,
        val languageRepository: LanguageRepository,
        val userStorage: UserStorage
) {

    fun getUserByFbToken(token: String): User? {

        if (token.isNullOrBlank()) return User.emptyUser

        val userMap: FbData = getUserFbData(token)
        this.userStorage.addUserFbData(token, userMap)
        return userRepository.findOneByFbId(userMap["id"] as String)
    }

    fun getToken(): String {
        val principal = SecurityContextHolder.getContext().authentication

        try {
            return (principal.details as OAuth2AuthenticationDetails).tokenValue
        } catch (e: Exception) {
            return ""
        }
    }

    fun createUser(token: String?) {

        if (token.isNullOrBlank()) return

        token?.let {
            val userMap = getUserFbData(token)

            if (userMap["id"] == null || userMap["name"] == null) {
                logger.warn("Not found FbData for token $token")
                return
            }

            val settings = getInitSettingsAndSave(userMap["id"] as String, token)
            val role = roleRepository.findOneByRoleName("user")

            val user: User = User()
            user.fbId = userMap["id"] as String
            user.name = userMap["name"] as String
            user.userSettings = settings
            user.role = role
            userRepository.save(user)
        }
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
                val response = createRequest("https://graph.facebook.com/me?access_token=$token")
                val userMap: FbData = response.body
                userStorage.addUserFbData(token, userMap)
            }
        } finally {
            return userStorage.getUserFbDataByToken(token)
        }
    }

    private fun getInitSettingsAndSave(userId: String, token: String): UserSettings {
        val response = createRequest("https://graph.facebook.com/v2.9/$userId?fields=email&access_token=$token")
        val userMap: FbData = response.body

        val userSetting = UserSettings()
        userSetting.infoMail = userMap["email"] as String
        userSetting.language = languageRepository.findOne(1) // POLISH FOR NOW
        userSettingRepository.save(userSetting)
        return userSetting
    }

    private fun createRequest(url: String): ResponseEntity<FbData> {
        val endpoint = URI.create(url)
        val request = RequestEntity<Any>(HttpMethod.GET, endpoint)
        val respType = object : ParameterizedTypeReference<FbData>() {}
        val response = restTemplate.exchange(request, respType)
        return response
    }

    companion object : KLogging()
}