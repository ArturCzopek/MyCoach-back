package pl.arturczopek.mycoach.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.arturczopek.mycoach.exception.NotFoundUserException
import pl.arturczopek.mycoach.model.database.User

@Service
open class UserStorage(
        private val currentUsers: MutableMap<String, User>,
        private val currentFbData: MutableMap<String, FbData>,
        private val dictionaryService: DictionaryService
) {

    @Autowired
    constructor(dictionaryService: DictionaryService) : this(mutableMapOf<String, User>(), mutableMapOf<String, FbData>(), dictionaryService)

    fun getUsers() = currentUsers

    @Throws(NotFoundUserException::class)
    fun getUserByToken(token: String?): User {
        if (token.isNullOrBlank()) return User.emptyUser

        try {
            return this.currentUsers[token] as User
        } catch(e: Exception) {
            throw NotFoundUserException(this.dictionaryService.translate("global.error.noLoggedInUserException.message", 1L).value)
        }
    }

    @Throws(NotFoundUserException::class)
    fun getUserFbDataByToken(token: String?): FbData {

        if (token.isNullOrBlank()) return FbData()

        try {
            return this.currentFbData[token] as FbData
        } catch(e: Exception) {
            throw NotFoundUserException(this.dictionaryService.translate("global.error.noLoggedInUserException.message", 1L).value)
        }
    }

    fun addUser(token: String?, user: User?) {

        if (token.isNullOrBlank()) return

        token?.let {
            if (user != null) {
                this.currentUsers[token] = user
            } else {
                this.currentUsers[token] = User.emptyUser
            }
        }
    }

    fun addUserFbData(token: String?, fbData: FbData?) {

        if (token.isNullOrBlank()) return

        token?.let {
            if (fbData != null) {
                this.currentFbData[token] = fbData
            } else {
                this.currentFbData[token] = FbData()
            }
        }
    }

    fun clearUser(token: String?) {

        if (token.isNullOrBlank()) return

        token?.let {
            this.currentUsers.remove(token)
            this.currentFbData.remove(token)
        }
    }
}