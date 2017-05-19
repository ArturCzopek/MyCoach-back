package pl.arturczopek.mycoach.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
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
typealias FbLanguageAndEmail = HashMap<String, String>
typealias FbData = Map<String, Any>

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
        val settings = getInitSettingsAndSave()

        val user: User = User()
        user.fbId = userMap["id"] as String
        user.name = userMap["name"] as String
        user.userSetting = settings
        userRepository.save(user)
    }

    private fun getInitSettingsAndSave(): UserSetting {
        val settings = UserSetting()
        settings.language = languageRepository.findOne(1)
        settings.infoMail = "test@test.com"
        userSettingRepository.save(settings)
        return settings
    }

    private fun getUserFbData(token: String): FbData {
        val endpoint = URI.create("https://graph.facebook.com/me?access_token=$token")
        val request = RequestEntity<Any>(HttpMethod.GET, endpoint)
        val respType = object : ParameterizedTypeReference<FbData>() {}
        val response = restTemplate.exchange(request, respType)
        val userMap: FbData = response.body
        return userMap
    }
}