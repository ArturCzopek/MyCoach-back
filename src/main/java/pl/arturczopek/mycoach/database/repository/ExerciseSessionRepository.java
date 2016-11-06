package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.database.entity.ExerciseSession;

/**
 * @Author Artur Czopek
 * @Date 06-11-2016
 */
public interface ExerciseSessionRepository extends CrudRepository<ExerciseSession, Long> {
}
