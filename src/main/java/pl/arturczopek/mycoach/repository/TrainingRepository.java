package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.model.database.Training;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface TrainingRepository extends CrudRepository<Training, Long> {

    void deleteBySetId(long setId);
}
