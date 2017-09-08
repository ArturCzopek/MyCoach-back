package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonFormat
import java.sql.Date

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

data class NewPrice(
        val productId: Long,
        val value: Float,
        val quantity: Float,
        val place: String?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") val priceDate: Date
)
