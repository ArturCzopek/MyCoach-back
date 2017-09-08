package pl.arturczopek.mycoach.model.add

/**
 * @Author Artur Czopek
 * @Date 14-04-2017
 */

data class NewExercise(
        val setId: Long,
        val exerciseName: String,
        val exerciseDescription: String?
)
