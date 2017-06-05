package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.PagingAndSortingRepository
import pl.arturczopek.mycoach.model.database.User

/**
 * @Author Artur Czopek
 * @Date 19-05-2017
 */
interface UserRepository: PagingAndSortingRepository<User, Long> {

    fun findOneByFbId(fbId: String): User
}