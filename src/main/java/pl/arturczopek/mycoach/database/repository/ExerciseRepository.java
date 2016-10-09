package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.database.entity.Exercise;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
}
