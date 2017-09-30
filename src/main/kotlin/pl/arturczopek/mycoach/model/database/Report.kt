package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.sql.Date
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Entity
@Table(name = "REPORTS")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Report(
        @Id
        @Column(name = "REP_ID", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "REPORTS_REP_ID_SEQ")
        @SequenceGenerator(name = "REPORTS_REP_ID_SEQ", sequenceName = "REPORTS_REP_ID_SEQ", allocationSize = 1)
        var reportId: Long = 0,

        @Column(name = "REP_CONT", nullable = false, length = 10000)
        var content: String = "",

        @Column(name = "REP_STRT_DT", nullable = false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        var startDate: Date = Date.valueOf("1970-01-01"),

        @Column(name = "REP_END_DT", nullable = false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        var endDate: Date = Date.valueOf("1970-01-01"),

        @JsonIgnore
        @Column(name = "REP_USR_ID")
        var userId: Long
)
