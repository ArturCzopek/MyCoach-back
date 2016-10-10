package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.database.entity.Report;

import java.util.List;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */

public interface ReportRepository extends PagingAndSortingRepository<Report, Long> {

    @Override
    List<Report> findAll();

    List<Report> findAllByOrderByEndDateDesc(Pageable pageable);
}
