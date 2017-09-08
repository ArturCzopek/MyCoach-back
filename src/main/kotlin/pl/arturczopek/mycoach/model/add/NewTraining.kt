package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonFormat
import java.sql.Date

/**
 * @Author Artur Czopek
 * @Date 14-04-2017
 */

data class NewTraining(
        val setId: Long,
        val exerciseSessions: List<NewExerciseSession>,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") val trainingDate: Date
)
