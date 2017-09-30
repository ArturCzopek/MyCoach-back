package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.sql.Date
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Entity
@Table(name = "PRICES")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Price(

        @Id
        @Column(name = "PRC_ID", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PRICES_PRC_ID_SEQ")
        @SequenceGenerator(name = "PRICES_PRC_ID_SEQ", sequenceName = "PRICES_PRC_ID_SEQ", allocationSize = 1)
        var priceId: Long = 0,

        @Column(name = "PRC_PRD_ID", nullable = false)
        var productId: Long = 0,

        @Column(name = "PRC_DT", nullable = false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        var priceDate: Date = Date.valueOf("1970-01-01"),

        @Column(name = "PRC_VAL", nullable = false)
        var value: Float = 0.0f,

        @Column(name = "PRC_QNT")
        var quantity: Float = 0.0f,

        @Column(name = "PRC_PLC", nullable = false, length = 60)
        var place: String = ""
)