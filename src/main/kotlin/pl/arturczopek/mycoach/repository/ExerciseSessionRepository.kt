package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.CrudRepository
import pl.arturczopek.mycoach.model.database.ExerciseSession

/**
 * @Author Artur Czopek
 * @Date 06-11-2016
 */

interface ExerciseSessionRepository : CrudRepository<ExerciseSession, Long>
