package pl.arturczopek.mycoach.model.database

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 19-05-2017
 */
@Entity
@Table(name = "USERS")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
class User {

    @Id
    @Column(name = "USR_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USERS_USR_ID_SEQ")
    @SequenceGenerator(name = "USERS_USR_ID_SEQ", sequenceName = "USERS_USR_ID_SEQ", allocationSize = 1)
    var userId: Long = 0

    @Column(name = "USR_FB_ID", nullable = false)
    lateinit var fbId: String

    @Column(name = "USR_NM", nullable = false)
    lateinit var name: String

    @OneToOne
    @JoinColumn(name = "USR_USS_ID")
    lateinit var userSetting: UserSetting

    @ManyToOne
    @JoinColumn(name = "USR_RL_ID")
    lateinit var role: Role
}