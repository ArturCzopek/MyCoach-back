package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.model.database.Cycle;

import java.sql.Date;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface CycleRepository extends PagingAndSortingRepository<Cycle, Long> {

    @Override
    List<Cycle> findAll();

    List<Cycle> findAllByOrderByIsFinishedDescEndDateAsc();

    long countByIsFinishedFalse();

    Cycle findOneByIsFinishedFalse();

    Cycle findFirstByOrderByEndDateDesc();

    Cycle findFirstByEndDateBeforeOrderByEndDateDesc(Date startDate);

    Cycle findFirstByStartDateAfterOrderByStartDate(Date startDate);
}
