package pl.arturczopek.mycoach.model.database

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 26-05-2017
 */

@Entity
@Table(name = "ROLES")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
class Role {

    @Id
    @Column(name = "RL_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ROLES_RL_ID_SEQ")
    @SequenceGenerator(name = "ROLES_RL_ID_SEQ", sequenceName = "ROLES_RL_ID_SEQ", allocationSize = 1)
    var roleId: Long = 0

    @Column(name = "RL_NM", nullable = false)
    lateinit var roleName: String
}