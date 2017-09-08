package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonFormat
import java.sql.Date

/**
 * @Author Artur Czopek
 * @Date 14-04-2017
 */

data class NewWeight(
        val value: Float,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") val measurementDate: Date
)
