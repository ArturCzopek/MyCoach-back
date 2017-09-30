package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Entity
@Table(name = "SERIES")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Series(

        @Id
        @Column(name = "SER_ID", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SERIES_SER_ID_SEQ")
        @SequenceGenerator(name = "SERIES_SER_ID_SEQ", sequenceName = "SERIES_SER_ID_SEQ", allocationSize = 1)
        var seriesId: Long = 0,

        @Column(name = "SER_EXS_ID")
        var exerciseSessionId: Long = 0,

        @Column(name = "SER_WGH", nullable = false)
        var weight: Float = 0.0f,

        @Column(name = "SER_REP", nullable = false)
        var repeats: Int = 0,

        @Column(name = "SER_COM", length = 250)
        var comment: String = ""
)
