package pl.arturczopek.mycoach.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pl.arturczopek.mycoach.model.database.UserSetting

/**
 * @Author arturczopek
 * @Date 30-06-2017
 */

interface UserSettingRepository: CrudRepository<UserSetting, Long> {

    @Query("select us.infoMail from UserSetting us")
    fun findAllEmails(): List<String>
}