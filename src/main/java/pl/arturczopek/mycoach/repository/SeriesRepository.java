package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.model.database.Series;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface SeriesRepository extends CrudRepository<Series, Long> {

    void deleteByExerciseSessionId(long exerciseSessionId);
}
