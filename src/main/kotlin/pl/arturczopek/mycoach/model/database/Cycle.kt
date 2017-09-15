package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.sql.Date
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Entity
@Table(name = "CYCLES")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Cycle(
        @Id
        @Column(name = "CYC_ID", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "CYCLES_CYC_ID_SEQ")
        @SequenceGenerator(name = "CYCLES_CYC_ID_SEQ", sequenceName = "CYCLES_CYC_ID_SEQ", allocationSize = 1)
        var cycleId: Long = 0,

        @Column(name = "CYC_FNSHD", nullable = false)
        var isFinished: Boolean = false,

        @Column(name = "CYC_STRT_DT", nullable = false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        var startDate: Date,

        @Column(name = "CYC_END_DT")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        var endDate: Date? = null,

        @OneToMany(cascade = arrayOf(CascadeType.ALL))
        @JoinColumn(name = "SET_CYC_ID")
        @LazyCollection(LazyCollectionOption.FALSE)
        val sets: MutableList<Set> = mutableListOf(),

        @JsonIgnore
        @Column(name = "CYC_USR_ID")
        var userId: Long = 0
)

