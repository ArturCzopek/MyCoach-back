package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.database.entity.Weight;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */
public interface WeightRepository extends PagingAndSortingRepository<Weight, Long> {

    @Override
    List<Weight> findAll();

    List<Weight> findAllByOrderByMeasurementDateDesc();

    List<Weight> findByMeasurementDateAfterAndMeasurementDateBefore(Timestamp after, Timestamp before);
}
