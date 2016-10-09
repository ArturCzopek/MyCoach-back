package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.database.entity.Series;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */
public interface SeriesRepository extends CrudRepository<Series, Long> {
}
