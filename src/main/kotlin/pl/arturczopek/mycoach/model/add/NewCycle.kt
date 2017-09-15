package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonFormat
import java.sql.Date

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

data class NewCycle(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") val startDate: Date?,
    val sets: List<NewSet>
)
