package pl.arturczopek.mycoach.model.database;

import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */

@Entity
@Table(name = "LANGUAGES")
data class Language(

        @Id
        @Column(name = "LNG_ID", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "LANGUAGES_LNG_ID")
        @SequenceGenerator(name = "LANGUAGES_LNG_ID", sequenceName = "LANGUAGES_LNG_ID", allocationSize = 1)
        var languageId: Long = 0,

        @Column(name = "LNG_SHRT_NAME", nullable = false, length = 3)
        var shortName: String = "",

        @Column(name = "LNG_FULL_NAME", nullable = false, length = 20)
        var fullName: String = ""
)

