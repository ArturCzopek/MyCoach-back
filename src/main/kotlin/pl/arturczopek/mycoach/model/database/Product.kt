package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.math.BigDecimal
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Entity
@Table(name = "PRODUCTS")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Product(

        @Id
        @Column(name = "PRD_ID", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PRODUCTS_PRD_ID_SEQ")
        @SequenceGenerator(name = "PRODUCTS_PRD_ID_SEQ", sequenceName = "PRODUCTS_PRD_ID_SEQ", allocationSize = 1)
        var productId: Long = 0,

        @Column(name = "PRD_NAME", nullable = false, length = 60)
        var productName: String = "",

        @Column(name = "PRD_SCRN", length = 100)
        @Lob
        var screen: Array<Byte> = emptyArray(),

        @Transient
        var average: Float = 0.0f,

        @JsonIgnore
        @OneToMany(cascade = arrayOf(CascadeType.ALL))
        @JoinColumn(name = "PRC_PRD_ID")
        @LazyCollection(LazyCollectionOption.FALSE)
        var prices: List<Price> = mutableListOf(),
        @JsonIgnore
        @Column(name = "PRD_USR_ID")
        var userId: Long = 0) {

    fun countAveragePrice() {

        var pricesSum = BigDecimal(0).setScale(2, BigDecimal.ROUND_UP)
        var quantitySum = BigDecimal(0).setScale(2, BigDecimal.ROUND_UP)

        if (prices.isNotEmpty()) {
            prices.forEach {
                pricesSum += BigDecimal.valueOf(it.value.toDouble())
                quantitySum += BigDecimal.valueOf(it.quantity.toDouble())
            }

            this.average = pricesSum.divide(quantitySum, 2, BigDecimal.ROUND_UP).toFloat()
        } else {
            this.average = 0.0f
        }
    }
}
