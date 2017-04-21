package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.model.database.Exercise;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface ExerciseRepository extends CrudRepository<Exercise, Long> {

    void deleteBySetId(long setId);
}
