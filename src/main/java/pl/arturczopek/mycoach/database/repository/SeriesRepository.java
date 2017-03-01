package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.database.entity.Series;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface SeriesRepository extends CrudRepository<Series, Long> {
}