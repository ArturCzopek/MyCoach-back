package pl.arturczopek.mycoach.model.add;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

data class NewExerciseSession(
        val exerciseId: Long,
        val series: List<NewSeries>,
        val isEmpty: Boolean?
)
