package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.model.database.Report;

import java.sql.Date;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface ReportRepository extends PagingAndSortingRepository<Report, Long> {

    @Override
    List<Report> findAll();

    List<Report> findAllByUserId(long userId);

    List<Report> findAllByUserIdOrderByEndDate(long userId);

    Report findFirstByEndDateBeforeAndReportIdNotOrderByEndDateDesc(Date date, long reportId);

    Report findFirstByStartDateAfterAndReportIdNotOrderByStartDate(Date date, long reportId);
}
