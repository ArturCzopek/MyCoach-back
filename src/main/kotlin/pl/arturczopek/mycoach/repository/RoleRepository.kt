package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.CrudRepository
import pl.arturczopek.mycoach.model.database.Role

/**
 * @Author Artur Czopek
 * @Date 26-05-2017
 */

interface RoleRepository: CrudRepository<Role, Int> {

    fun findOneByRoleName(roleName: String): Role
}