package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.database.entity.Report;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */
public interface ReportRepository extends CrudRepository<Report, Long> {
}
