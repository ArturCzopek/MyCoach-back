package pl.arturczopek.mycoach.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pl.arturczopek.mycoach.model.database.User
import pl.arturczopek.mycoach.model.database.UserSetting
import pl.arturczopek.mycoach.repository.LanguageRepository
import pl.arturczopek.mycoach.repository.UserRepository
import pl.arturczopek.mycoach.repository.UserSettingRepository
import java.net.URI

/**
 * @Author Artur Czopek
 * @Date 19-05-2017
 */
typealias FbData = HashMap<String, Any>

@Service class UserService
@Autowired constructor(
        val restTemplate: RestTemplate,
        val userRepository: UserRepository,
        val userSettingRepository: UserSettingRepository,
        val languageRepository: LanguageRepository) {

    fun getFbUserByToken(token: String): User {
        val userMap: FbData = getUserFbData(token)

        val user = userRepository.findOneByFbId(userMap["id"] as String)
        return user
    }

    fun createUser(token: String) {
        val userMap = getUserFbData(token)
        val settings = getInitSettingsAndSave(userMap["id"] as String, token)

        val user: User = User()
        user.fbId = userMap["id"] as String
        user.name = userMap["name"] as String
        user.userSetting = settings
        userRepository.save(user)
    }

    private fun getUserFbData(token: String): FbData {
        val response = createRequest("https://graph.facebook.com/me?access_token=$token")
        val userMap: FbData = response.body
        return userMap
    }

    private fun getInitSettingsAndSave(userId: String, token: String): UserSetting {
        val response = createRequest("https://graph.facebook.com/v2.9/$userId?fields=email&access_token=$token")
        val userMap: FbData = response.body

        val userSetting = UserSetting()
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
}