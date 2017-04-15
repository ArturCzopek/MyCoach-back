package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.model.database.Report;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface ReportRepository extends PagingAndSortingRepository<Report, Long> {

    @Override
    List<Report> findAll();

    List<Report> findAllByOrderByEndDate();
}
