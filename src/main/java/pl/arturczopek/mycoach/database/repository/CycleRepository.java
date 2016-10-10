package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.database.entity.Cycle;

import java.util.List;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */
public interface CycleRepository extends PagingAndSortingRepository<Cycle, Long> {

    @Override
    List<Cycle> findAll();

    List<Cycle> findAllByOrderByEndDateDesc();
}
