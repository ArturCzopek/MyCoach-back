package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.model.database.Cycle;
import pl.arturczopek.mycoach.model.database.Set;

import java.sql.Date;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface CycleRepository extends PagingAndSortingRepository<Cycle, Long> {

    @Override
    List<Cycle> findAll();

    List<Cycle> findAllByUserIdOrderByIsFinishedDescEndDateAsc(long userId);

    long countByUserIdAndIsFinishedFalse(long userId);

    Cycle findOneByUserIdAndIsFinishedFalse(long userId);

    Cycle findFirstByUserIdOrderByEndDateDesc(long userId);

    Cycle findFirstByUserIdAndEndDateBeforeOrderByEndDateDesc(long userId, Date startDate);

    Cycle findFirstByUserIdAndStartDateAfterOrderByStartDate(long userId, Date startDate);

    Cycle findOneBySetsContains(Set set);
}
