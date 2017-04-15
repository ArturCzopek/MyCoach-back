package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.model.database.Cycle;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface CycleRepository extends PagingAndSortingRepository<Cycle, Long> {

    @Override
    List<Cycle> findAll();

    List<Cycle> findAllByOrderByEndDateDesc();

    Cycle findOneByIsFinishedFalse();
}
