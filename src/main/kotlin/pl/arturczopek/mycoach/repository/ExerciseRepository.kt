package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.CrudRepository
import pl.arturczopek.mycoach.model.database.Exercise

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

interface ExerciseRepository : CrudRepository<Exercise, Long>
