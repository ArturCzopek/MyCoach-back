package pl.arturczopek.mycoach.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pl.arturczopek.mycoach.model.database.UserSettings

/**
 * @Author Artur Czopek
 * @Date 30-06-2017
 */

interface UserSettingRepository: CrudRepository<UserSettings, Long> {

    @Query("select us.infoMail from UserSettings us")
    fun findAllEmails(): List<String>
}