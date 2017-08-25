package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.Data
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Data
@Entity
@Table(name = "WEIGHTS")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
class Weight : Serializable {

    @Id
    @Column(name = "WGH_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "WEIGHTS_WGH_ID_SEQ")
    @SequenceGenerator(name = "WEIGHTS_WGH_ID_SEQ", sequenceName = "WEIGHTS_WGH_ID_SEQ", allocationSize = 1)
    var weightId: Long = 0

    @Column(name = "WGH_VAL", nullable = false)
    var value: Float = 0f

    @Column(name = "WGH_MSRM_DT", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    lateinit var measurementDate: LocalDate

    @JsonIgnore
    @Column(name = "WGH_USR_ID")
    var userId: Long = 0

    companion object {
        val serialVersionUID = -4251427345815202610L
    }
}
